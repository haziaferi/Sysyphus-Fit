package com.hazia.sisyphusfit.data

import com.hazia.sisyphusfit.core.Module
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

/** One session model (N-SESSION), logged per calendar date (FA13), resumable after the app is killed (GL1, 0.27). */
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
