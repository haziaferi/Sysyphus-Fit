package com.hazia.sisyphusfit.data

import androidx.room.ConstructedBy
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

const val SCHEMA_VERSION = 2
const val DATABASE_NAME = "sisyphusfit.db"

@Database(
    entities = [
        ExerciseEntity::class, RoutineEntity::class, RoutineItemEntity::class, SessionEntity::class,
        LoggedSetEntity::class, ProgressionRuleEntity::class, IntervalPresetEntity::class,
        TrackPointEntity::class, GoalEntity::class, SettingEntity::class,
        ExerciseModuleEntity::class, LabelEntity::class, SessionLabelEntity::class, BodyMeasureEntity::class,
    ],
    version = SCHEMA_VERSION,
    exportSchema = true,
)
@ConstructedBy(SisyphusDatabaseConstructor::class)
abstract class SisyphusDatabase : RoomDatabase() {
    abstract fun floorDao(): FloorDao
    abstract fun settingDao(): SettingDao
    abstract fun exerciseDao(): ExerciseDao
}

// The Room compiler generates the actual implementations.
@Suppress("NO_ACTUAL_FOR_EXPECT", "KotlinNoActualForExpect")
expect object SisyphusDatabaseConstructor : RoomDatabaseConstructor<SisyphusDatabase>

/** The floor's writes and reads (SPEC §2). Features add their own DAOs as their stage is built. */
@Dao
interface FloorDao {
    @Insert suspend fun addExercise(row: ExerciseEntity)
    @Insert suspend fun addRoutine(row: RoutineEntity)
    @Insert suspend fun addRoutineItem(row: RoutineItemEntity)
    @Insert suspend fun addSession(row: SessionEntity)
    @Insert suspend fun addSets(rows: List<LoggedSetEntity>)
    @Insert suspend fun addExerciseModules(rows: List<ExerciseModuleEntity>)
    @Insert suspend fun addLabel(row: LabelEntity)
    @Insert suspend fun addSessionLabels(rows: List<SessionLabelEntity>)
    @Insert suspend fun addBodyMeasure(row: BodyMeasureEntity)

    @Query("SELECT * FROM exercise WHERE archived = 0 ORDER BY name, id")
    suspend fun exercises(): List<ExerciseEntity>

    @Query("SELECT * FROM routine_item WHERE routine_id = :routineId ORDER BY position, id")
    suspend fun routineItems(routineId: String): List<RoutineItemEntity>

    @Query("SELECT * FROM session WHERE day BETWEEN :from AND :to ORDER BY day, started_at, id")
    suspend fun sessionsBetween(from: String, to: String): List<SessionEntity>

    /** A session left OPEN (the app was closed mid-workout), to resume. */
    @Query("SELECT * FROM session WHERE state = 'OPEN' ORDER BY started_at DESC, id DESC LIMIT 1")
    suspend fun openSession(): SessionEntity?

    /**
     * The sets of the last FINISHED or STOPPED_EARLY session that did [exerciseId], in order:
     * what stage 1's LL2 and GM2 show beside a set and when picking an exercise.
     */
    @Query(
        """
        SELECT s.* FROM logged_set s
        WHERE s.exercise_id = :exerciseId AND s.session_id = (
            SELECT ls.session_id FROM logged_set ls JOIN session se ON se.id = ls.session_id
            WHERE ls.exercise_id = :exerciseId AND se.state != 'OPEN'
            ORDER BY ls.done_at DESC, ls.id DESC LIMIT 1
        )
        ORDER BY s.position, s.id
        """,
    )
    suspend fun lastSets(exerciseId: String): List<LoggedSetEntity>
}
