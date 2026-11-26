package com.hazia.sisyphusfit.data

import com.hazia.sisyphusfit.core.Module
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

private const val MINUTE = 60_000L
private const val HOUR = 60 * MINUTE
private const val TUE_1900 = 1_761_678_000_000L // 2025-10-28T19:00Z, a Tuesday

/** One session model (N-SESSION), logged per calendar date (FA13), resumable after the app is killed (GL1, SPEC:R34). */
class SessionsTest {
    private val temp = TempDatabase()
    private val db = temp.db
    private val time = TestClock()
    private val store = SessionStore(db = { db }, clock = time.clock)
    private val exercises = ExerciseStore(db = { db }, clock = time.clock)

    @AfterTest fun close() = temp.close()

    private suspend fun exercise(name: String = "Bench press") = exercises.add(name, ExerciseKind.DYNAMIC, false, setOf(Module.STRENGTH))

    @Test fun startCountsOnTheLocalDayOfItsZone() = runBlocking {
        time.now = 1_761_692_400_000 // 2025-10-28T23:00Z, already the 29th in Rome
        time.zone = "Europe/Rome"
        store.start()
        assertEquals("2025-10-29", store.open().first()!!.session.day)
    }

    @Test fun dayDoesNotMoveWhenTheZoneChanges() = runBlocking {
        time.now = 1_761_692_400_000
        val id = store.start()
        time.zone = "America/New_York"
        time.now += 60_000
        store.finish(id)
        assertEquals("2025-10-29", db.floorDao().sessionsBetween("2025-10-01", "2025-10-31").single().day)
    }

    // M3 (a): a second start (a double tap, a race with resume) resumes the open session instead of failing.
    @Test fun startingWhileOneIsOpenResumesIt() = runBlocking {
        val first = store.start()
        time.now += 60_000
        assertEquals(first, store.start(activity = "walk"))
        assertEquals(1L, db.count("SELECT count(*) FROM session"))
        assertNull(store.open().first()!!.session.activity)
    }

    @Test fun setsTakeTheNextPositionInTheSession() = runBlocking {
        val a = exercise("Squat")
        val b = exercise("Row")
        val s = store.start()
        listOf(a, b, a).forEach { store.logSet(s, it, SetValues(reps = 5)) }
        assertEquals(listOf(0, 1, 2), store.sets(s).first().map { it.position })
    }

    // SPEC:R121: a set keeps the type its exercise had when it was logged; changing the type later (R101) leaves it.
    @Test fun aSetKeepsTheTypeItWasLoggedUnder() = runBlocking {
        val ex = exercise("Hollow hold")
        val s = store.start()
        store.logSet(s, ex, SetValues(reps = 5))
        exercises.update(exercises.active().first().single().copy(kind = ExerciseKind.ISOMETRIC))
        store.logSet(s, ex, SetValues(seconds = 30))
        assertEquals(listOf("DYNAMIC", "ISOMETRIC"), store.sets(s).first().map { it.kind })
    }

    @Test fun partialRepsStayTheirOwnCount() = runBlocking {
        val s = store.start()
        store.logSet(s, exercise(), SetValues(reps = 7, partialReps = 1))
        val row = store.sets(s).first().single()
        assertEquals(7 to 1, row.reps to row.partialReps)
    }

    @Test fun negativeValuesAreRefused() {
        assertFailsWith<IllegalArgumentException> { SetValues(reps = -1) }
        assertFailsWith<IllegalArgumentException> { SetValues(weightGrams = -500) }
        assertFailsWith<IllegalArgumentException> { SetValues(partialReps = -1) }
    }

    // The app killed mid-session: a new store on the same file finds the session and its sets.
    @Test fun openSessionResumesWithItsSets() = runBlocking {
        val s = store.start()
        store.logSet(s, exercise(), SetValues(reps = 5, weightGrams = 60_000))
        val again = temp.reopen()
        try {
            val resumed = SessionStore(db = { again }, clock = time.clock).open().first()!!
            assertEquals(s, resumed.session.id)
            assertEquals(listOf(60_000L), resumed.sets.map { it.weightGrams })
        } finally {
            again.close()
        }
    }

    @Test fun finishedSessionIsNoLongerOpenAndTakesNoSets() {
        val (s, ex) = runBlocking {
            val s = store.start()
            val ex = exercise()
            store.finish(s)
            s to ex
        }
        assertNull(runBlocking { store.open().first() })
        assertFailsWith<IllegalStateException> { runBlocking { store.logSet(s, ex, SetValues(reps = 1)) } }
        assertFailsWith<IllegalStateException> { runBlocking { store.finish(s) } }
    }

    @Test fun stoppedEarlyKeepsOnlyDoneSets() = runBlocking {
        val s = store.start()
        store.logSet(s, exercise(), SetValues(reps = 5))
        time.now = 9_000
        store.finish(s, stoppedEarly = true)
        val row = db.floorDao().sessionsBetween("0000-01-01", "9999-12-31").single()
        assertEquals("STOPPED_EARLY" to 9_000L, row.state to row.endedAt)
        assertEquals(1, store.sets(s).first().size)
    }

    // SPEC:R96: "End/Save" the next morning ends the session at its last set, not at the tap.
    @Test fun endAndSaveEndsAtTheLastSet() = runBlocking {
        val s = store.start()
        time.now = 1_000_000
        store.logSet(s, exercise(), SetValues(reps = 5))
        time.now = 1_000_000 + 14 * HOUR
        store.endAndSave(s)
        val row = db.floorDao().sessionsBetween("0000-01-01", "9999-12-31").single()
        assertEquals("STOPPED_EARLY" to 1_000_000L, row.state to row.endedAt)
    }

    // SPEC:R96: a session without sets (a walk) ends at the time the person gives, never at its start.
    @Test fun aSessionWithoutSetsEndsAtTheTimeGiven() = runBlocking {
        time.now = 1_000_000
        val s = store.start(activity = "Walk")
        time.now += 10 * HOUR
        store.endAndSave(s, askedEnd = 1_000_000 + HOUR / 2)
        val row = db.floorDao().sessionsBetween("0000-01-01", "9999-12-31").single()
        assertEquals("STOPPED_EARLY" to 1_000_000 + HOUR / 2, row.state to row.endedAt)
    }

    @Test fun anEndBeforeTheStartIsRefused() {
        val s = runBlocking { time.now = 1_000_000; store.start(activity = "Walk") }
        assertFailsWith<IllegalArgumentException> { runBlocking { store.endAndSave(s, askedEnd = 999_999) } }
        assertEquals("OPEN", runBlocking { store.open().first()!!.session.state })
    }

    // SPEC:R96: Tuesday's session, last set 19:10, is closed there when Start opens on Wednesday at 09:00.
    @Test fun anIdleSessionFromAnEarlierDayIsClosedAtItsLastSet() = runBlocking {
        time.zone = "UTC"
        time.now = TUE_1900
        val s = store.start()
        time.now = TUE_1900 + 10 * MINUTE
        store.logSet(s, exercise(), SetValues(reps = 5))
        time.now = TUE_1900 + 14 * HOUR
        assertEquals(true, store.closeIfIdle())
        assertNull(store.open().first())
        assertEquals(TUE_1900 + 10 * MINUTE, db.floorDao().sessionsBetween("0000-01-01", "9999-12-31").single().endedAt)
    }

    // A session started at 23:30 with a set at 00:05 is still going at 00:15: it resumes.
    @Test fun aSessionStillGoingPastMidnightStaysOpen() = runBlocking {
        time.zone = "UTC"
        time.now = TUE_1900 + 4 * HOUR + 30 * MINUTE
        store.start()
        time.now = TUE_1900 + 5 * HOUR + 5 * MINUTE
        store.logSet(store.open().first()!!.session.id, exercise(), SetValues(reps = 5))
        time.now = TUE_1900 + 5 * HOUR + 15 * MINUTE
        assertEquals(false, store.closeIfIdle())
        assertEquals("OPEN", store.open().first()!!.session.state)
    }

    @Test fun anIdleSessionFromTodayStaysOpen() = runBlocking {
        time.zone = "UTC"
        time.now = TUE_1900 - 12 * HOUR
        store.logSet(store.start(), exercise(), SetValues(reps = 5))
        time.now = TUE_1900 + 4 * HOUR
        assertEquals(false, store.closeIfIdle())
    }

    // Without a set there is no time to end at: the resume card asks instead.
    @Test fun anIdleSessionWithoutSetsStaysOpen() = runBlocking {
        time.zone = "UTC"
        time.now = TUE_1900
        store.start(activity = "Walk")
        time.now = TUE_1900 + 14 * HOUR
        assertEquals(false, store.closeIfIdle())
    }

    // Review of PR #16: "over 3 h" (SPEC:R96) leaves a session idle exactly 3 h open.
    @Test fun exactlyThreeHoursIdleStaysOpen() = runBlocking {
        time.zone = "UTC"
        time.now = TUE_1900
        store.logSet(store.start(), exercise(), SetValues(reps = 5))
        time.now = TUE_1900 + 5 * HOUR
        store.logSet(store.open().first()!!.session.id, exercise("Row"), SetValues(reps = 5))
        time.now += 3 * HOUR
        assertEquals(false, store.closeIfIdle())
    }

    // R30 counts in the zone a session was recorded in: begun Tuesday 20:00 in Rome, it is from an earlier day once Rome
    // reaches Wednesday, even with the phone in New York, where it is still Tuesday.
    @Test fun anEarlierDayIsJudgedInTheSessionsZone() = runBlocking {
        time.zone = "Europe/Rome"
        time.now = TUE_1900
        val s = store.start()
        time.now = TUE_1900 + 10 * MINUTE
        store.logSet(s, exercise(), SetValues(reps = 5))
        time.zone = "America/New_York"
        time.now = TUE_1900 + 4 * HOUR
        assertEquals(true, store.closeIfIdle())
    }

    // Review of PR #16: a restore can bring two open sessions; an idle older one is closed too, not left for later.
    @Test fun everyIdleOpenSessionIsClosed() = runBlocking {
        time.zone = "UTC"
        time.now = TUE_1900
        val old = store.start()
        store.logSet(old, exercise(), SetValues(reps = 5))
        db.execute(
            "INSERT INTO session (id, routine_id, activity, started_at, ended_at, zone, day, state, intensity, note) " +
                "VALUES ('zz-today', NULL, 'Walk', ${TUE_1900 + 14 * HOUR}, NULL, 'UTC', '2025-10-29', 'OPEN', NULL, NULL)",
        )
        time.now = TUE_1900 + 15 * HOUR
        assertEquals(true, store.closeIfIdle())
        assertEquals(listOf("zz-today"), store.open().first().let { listOfNotNull(it?.session?.id) })
        assertEquals(1L, db.count("SELECT count(*) FROM session WHERE state = 'STOPPED_EARLY'"))
    }

    @Test fun anEndInTheFutureIsRefused() {
        val s = runBlocking { time.now = 1_000_000; store.start(activity = "Walk") }
        time.now = 2_000_000
        assertFailsWith<IllegalArgumentException> { runBlocking { store.endAndSave(s, askedEnd = 2_000_001) } }
    }

    // SPEC:R67: last time is the last session that logged the exercise under its current type, so reps never face seconds.
    @Test fun lastSetsAreThoseOfTheExercisesCurrentType() = runBlocking {
        val ex = exercise("Hollow hold")
        val a = store.start()
        store.logSet(a, ex, SetValues(reps = 8))
        store.finish(a)
        time.now += 1_000
        exercises.update(exercises.active().first().single().copy(kind = ExerciseKind.ISOMETRIC))
        assertEquals(emptyList(), store.lastSets(ex))

        val b = store.start()
        store.logSet(b, ex, SetValues(seconds = 30))
        store.finish(b)
        time.now += 1_000
        assertEquals(listOf(30), store.lastSets(ex).map { it.seconds })
        exercises.update(exercises.active().first().single().copy(kind = ExerciseKind.DYNAMIC))
        assertEquals(listOf(8), store.lastSets(ex).map { it.reps })
    }

    @Test fun lastSetsComeFromTheLastClosedSession() = runBlocking {
        val ex = exercise()
        val a = store.start()
        store.logSet(a, ex, SetValues(reps = 5))
        store.logSet(a, ex, SetValues(reps = 4))
        store.finish(a)
        time.now += 1_000
        val b = store.start()
        store.logSet(b, ex, SetValues(reps = 6))
        assertEquals(listOf(5, 4), store.lastSets(ex).map { it.reps })
    }
}
