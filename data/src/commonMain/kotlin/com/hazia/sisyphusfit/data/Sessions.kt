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

/** What a done set records; every count and measure is zero or more. Partial reps are their own count (0.33). */
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

/** The session left open (the app was closed mid-workout) with its sets in order, to resume (GL1, 0.27). */
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

    /** Starts [row] unless a session is already open, and returns the open one: resume (GL1) never chooses between two. */
    @Transaction
    open suspend fun start(row: SessionEntity): String = openId() ?: row.id.also { insert(row) }

    /** Adds a done set at the next position in its session, which must be open. */
    @Transaction
    open suspend fun addSet(row: LoggedSetEntity) {
        checkOpen(row.sessionId)
        insert(row.copy(position = nextPosition(row.sessionId)))
    }

    @Transaction
    open suspend fun finish(sessionId: String, endedAt: Long, state: String) {
        checkOpen(sessionId)
        close(sessionId, endedAt, state)
    }

    private suspend fun checkOpen(sessionId: String) =
        check(stateOf(sessionId) == "OPEN") { "session $sessionId is not open" }
}

class SessionStore(private val db: Db, private val clock: WallClock) {
    /** Starts a session now, or returns the one already open; it counts on the local day it starts, in the zone it starts in (FA13). */
    suspend fun start(routineId: String? = null, activity: String? = null): String {
        val now = clock.now()
        val zone = clock.zone()
        val id = Ids.ulid(now)
        val row = SessionEntity(id, routineId, activity, now, null, zone, countingDay(now, zone).toString(), "OPEN", null, null)
        return db().sessionDao().start(row)
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
        )
        db().sessionDao().addSet(row)
        return id
    }

    /** Ends an open session; stopped early keeps only the sets that were done (KA2). */
    suspend fun finish(sessionId: String, stoppedEarly: Boolean = false) =
        db().sessionDao().finish(sessionId, clock.now(), if (stoppedEarly) "STOPPED_EARLY" else "FINISHED")

    /** The sets of the last closed session that did [exerciseId]: last time, for the comparison (SG2). */
    suspend fun lastSets(exerciseId: String): List<LoggedSetEntity> = db().floorDao().lastSets(exerciseId)
}
