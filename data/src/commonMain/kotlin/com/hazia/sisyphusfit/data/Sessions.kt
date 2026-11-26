package com.hazia.sisyphusfit.data

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import com.hazia.sisyphusfit.core.Ids
import com.hazia.sisyphusfit.core.countingDay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

enum class Side { L, R }

/** What a done set records; every count and measure is zero or more. Partial reps are their own count (SPEC:R32). */
data class SetValues(
    val reps: Int? = null,
    val weightGrams: Long? = null,
    val seconds: Int? = null,
    val distanceMetres: Int? = null,
    val side: Side? = null,
    val partialReps: Int = 0,
    val power: Boolean = false,
) {
    init {
        require(listOfNotNull(reps, seconds, distanceMetres, partialReps).all { it >= 0 } && (weightGrams ?: 0) >= 0) {
            "a set cannot hold a negative value: $this"
        }
    }
}

/** The session left open (the app was closed mid-workout) with its sets in order, to resume (GL1, SPEC:R34). */
data class OpenSession(val session: SessionEntity, val sets: List<LoggedSetEntity>)

data class SessionWithSets(
    @Embedded val session: SessionEntity,
    @Relation(parentColumn = "id", entityColumn = "session_id") val sets: List<LoggedSetEntity>,
)

@Dao
abstract class SessionDao {
    @Transaction
    @Query("SELECT * FROM session WHERE state = 'OPEN' ORDER BY started_at DESC, id DESC LIMIT 1")
    abstract fun open(): Flow<SessionWithSets?>

    @Query("SELECT * FROM logged_set WHERE session_id = :sessionId ORDER BY position, id")
    abstract fun sets(sessionId: String): Flow<List<LoggedSetEntity>>

    // The same session open() resumes, should a restored backup have brought two.
    @Query("SELECT id FROM session WHERE state = 'OPEN' ORDER BY started_at DESC, id DESC LIMIT 1")
    protected abstract suspend fun openId(): String?

    @Query("SELECT state FROM session WHERE id = :sessionId")
    protected abstract suspend fun stateOf(sessionId: String): String?

    @Query("SELECT COALESCE(MAX(position), -1) + 1 FROM logged_set WHERE session_id = :sessionId")
    protected abstract suspend fun nextPosition(sessionId: String): Int

    @Insert protected abstract suspend fun insert(row: SessionEntity)
    @Insert protected abstract suspend fun insert(row: LoggedSetEntity)

    @Query("UPDATE session SET ended_at = :endedAt, state = :state WHERE id = :sessionId")
    protected abstract suspend fun close(sessionId: String, endedAt: Long, state: String)

    /**
     * Closes or drops the sessions idle at [now] (SPEC:R87), then starts [row] unless a session is still open, and returns
     * the open one: resume (GL1) never chooses between two.
     */
    @Transaction
    open suspend fun start(row: SessionEntity, now: Long, idleMs: Long): String {
        closeIfIdle(now, idleMs)
        return openId() ?: row.id.also { insert(row) }
    }

    @Query("SELECT kind FROM exercise WHERE id = :exerciseId")
    protected abstract suspend fun kindOf(exerciseId: String): String?

    /** Adds a done set at the next position in its session, which must be open, under its exercise's type now (SPEC:R121). */
    @Transaction
    open suspend fun addSet(row: LoggedSetEntity) {
        checkOpen(row.sessionId)
        val kind = checkNotNull(kindOf(row.exerciseId)) { "no exercise ${row.exerciseId}" }
        insert(row.copy(position = nextPosition(row.sessionId), kind = kind))
    }

    @Query("SELECT MAX(done_at) FROM logged_set WHERE session_id = :sessionId")
    protected abstract suspend fun lastSetAt(sessionId: String): Long?

    @Transaction
    open suspend fun finish(sessionId: String, endedAt: Long, state: String) {
        checkOpen(sessionId)
        close(sessionId, endedAt, state)
    }

    @Query("SELECT started_at FROM session WHERE id = :sessionId")
    protected abstract suspend fun startedAt(sessionId: String): Long?

    /**
     * Stops [sessionId] early at its last set or track point, or, with neither, at [askedEnd], which must lie between the
     * session's start and [now] (SPEC:R96).
     */
    @Transaction
    open suspend fun stopAtLastSet(sessionId: String, askedEnd: Long?, now: Long) {
        checkOpen(sessionId)
        val end = lastMoment(sessionId) ?: requireNotNull(askedEnd) { "session $sessionId has no set: ask when it ended" }
            .also { require(it >= (startedAt(sessionId) ?: it)) { "an end before the session's start" } }
            .also { require(it <= now) { "an end in the future" } }
        close(sessionId, end, "STOPPED_EARLY")
    }

    @Query("SELECT * FROM session WHERE state = 'OPEN'")
    protected abstract suspend fun allOpen(): List<SessionEntity>

    @Query("SELECT MAX(t) FROM track_point WHERE session_id = :sessionId")
    protected abstract suspend fun lastPointAt(sessionId: String): Long?

    @Query("SELECT count(*) FROM session_label WHERE session_id = :sessionId")
    protected abstract suspend fun labelCount(sessionId: String): Int

    @Query("DELETE FROM session WHERE id = :sessionId")
    protected abstract suspend fun drop(sessionId: String)

    /** The session's last set or track point, whichever is later; null with neither. */
    private suspend fun lastMoment(sessionId: String): Long? = listOfNotNull(lastSetAt(sessionId), lastPointAt(sessionId)).maxOrNull()

    /**
     * Every open session idle for more than [idleMs] before [now], counted from its last set or track point or, with
     * none, from its start (SPEC:R96, R87): closed as stopped early at that last moment; with only a time of its own,
     * that long after its start, never after [now]; with nothing logged, or with no start and nothing logged, dropped. A
     * session with only a note, an intensity or labels waits for the resume card to ask when it ended, so they are kept.
     * True when one was closed or dropped.
     */
    @Transaction
    open suspend fun closeIfIdle(now: Long, idleMs: Long): Boolean {
        var changed = false
        for (s in allOpen()) {
            val last = lastMoment(s.id)
            val since = last ?: s.startedAt
            if (since != null && now - since <= idleMs) continue
            when {
                last != null -> close(s.id, last, "STOPPED_EARLY")
                s.seconds != null && s.startedAt != null -> close(s.id, minOf(s.startedAt + s.seconds * 1000L, now), "STOPPED_EARLY")
                s.seconds != null || s.note != null || s.intensity != null || labelCount(s.id) > 0 -> continue
                else -> drop(s.id)
            }
            changed = true
        }
        return changed
    }

    private suspend fun checkOpen(sessionId: String) =
        check(stateOf(sessionId) == "OPEN") { "session $sessionId is not open" }
}

/** How long a session may sit with nothing new before Start closes or drops it (SPEC:R96). */
private const val IDLE_MS = 3 * 60 * 60 * 1000L

class SessionStore(private val db: Db, private val clock: WallClock) {
    /**
     * Starts a session now, or returns the one already open; it counts on the local day it starts, in the zone it starts in
     * (FA13). An idle open session is closed or dropped first, so only one still in use is resumed (SPEC:R87).
     */
    suspend fun start(routineId: String? = null, activity: String? = null): String {
        val now = clock.now()
        val zone = clock.zone()
        val id = Ids.ulid(now)
        val row = SessionEntity(id, routineId, activity, now, null, zone, countingDay(now, zone).toString(), "OPEN", null, null)
        return db().sessionDao().start(row, now, IDLE_MS)
    }

    fun open(): Flow<OpenSession?> = flow { emitAll(db().sessionDao().open()) }.map { found ->
        found?.let { OpenSession(it.session, it.sets.sortedWith(compareBy({ s -> s.position }, { s -> s.id }))) }
    }

    fun sets(sessionId: String): Flow<List<LoggedSetEntity>> = flow { emitAll(db().sessionDao().sets(sessionId)) }

    suspend fun logSet(sessionId: String, exerciseId: String, values: SetValues): String {
        val now = clock.now()
        val id = Ids.ulid(now)
        val row = LoggedSetEntity(
            id = id, sessionId = sessionId, exerciseId = exerciseId, position = 0, side = values.side?.name,
            reps = values.reps, weightGrams = values.weightGrams, seconds = values.seconds,
            distanceMetres = values.distanceMetres, rpe = null, rir = null, doneAt = now,
            power = values.power, partialReps = values.partialReps,
            kind = "", // addSet writes the exercise's type, read in its own transaction
        )
        db().sessionDao().addSet(row)
        return id
    }

    /** Ends an open session; stopped early keeps only the sets that were done (KA2). */
    suspend fun finish(sessionId: String, stoppedEarly: Boolean = false) =
        db().sessionDao().finish(sessionId, clock.now(), if (stoppedEarly) "STOPPED_EARLY" else "FINISHED")

    /**
     * "End/Save" on the resume card (SPEC:R96): stopped early (KA2), ending at its last set's time, not at the tap. A session
     * without sets ends at [askedEnd], the time the screen asks for.
     */
    suspend fun endAndSave(sessionId: String, askedEnd: Long? = null) =
        db().sessionDao().stopAtLastSet(sessionId, askedEnd, clock.now())

    /**
     * What Start does when it opens (SPEC:R96): every open session idle for over 3 h, whatever its day, is stopped early at
     * its last set, or dropped when nothing was logged in it, so a later session's sets never join it (SPEC:R87). True when
     * one was closed or dropped.
     */
    suspend fun closeIfIdle(): Boolean = db().sessionDao().closeIfIdle(clock.now(), IDLE_MS)

    /** The sets of the last closed session that did [exerciseId]: last time, for the comparison (SG2). */
    suspend fun lastSets(exerciseId: String): List<LoggedSetEntity> = db().floorDao().lastSets(exerciseId)
}
