package com.hazia.sisyphusfit.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/*
 * Schema version 2 (SPEC §4): schema 1, designed for the whole 50-point scope, plus the additive
 * parts of stage 1a (module tags, labels, body measures, power and partial reps). Integers only:
 * epoch milliseconds UTC plus the recording zone, seconds, grams, millimetres, metres, 1e-7 degrees.
 * Ids are ULIDs from :core. A column added by a later schema goes LAST in its entity, with a SQL
 * default: ALTER TABLE appends, so a migrated table then matches a fresh one column for column.
 */

/**
 * kind: DYNAMIC, ISOMETRIC, TIMED, DISTANCE. sided: counted per side (harvest CM2).
 * show_partial_reps (schema 2, 0.33): the partial-reps count is offered on this exercise.
 */
@Entity(tableName = "exercise")
data class ExerciseEntity(
    @PrimaryKey val id: String,
    val name: String,
    val kind: String,
    val sided: Boolean,
    val archived: Boolean,
    @ColumnInfo(name = "show_partial_reps", defaultValue = "0") val showPartialReps: Boolean = false,
)

/** Schema 2 (0.23): a module tag on an exercise, several per exercise. module: a :core Module name. */
@Entity(
    tableName = "exercise_module",
    foreignKeys = [ForeignKey(ExerciseEntity::class, ["id"], ["exercise_id"], onDelete = ForeignKey.CASCADE)],
    indices = [Index(value = ["exercise_id", "module"], unique = true)],
)
data class ExerciseModuleEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "exercise_id") val exerciseId: String,
    val module: String,
)

/** weekdays: bit 0 = Monday ... bit 6 = Sunday, 0 = unscheduled (harvest CM3, KA6). */
@Entity(tableName = "routine")
data class RoutineEntity(
    @PrimaryKey val id: String,
    val name: String,
    val weekdays: Int,
    val position: Int,
)

@Entity(
    tableName = "routine_item",
    foreignKeys = [
        ForeignKey(RoutineEntity::class, ["id"], ["routine_id"], onDelete = ForeignKey.CASCADE),
        ForeignKey(ExerciseEntity::class, ["id"], ["exercise_id"], onDelete = ForeignKey.RESTRICT),
    ],
    indices = [Index("routine_id"), Index("exercise_id")],
)
data class RoutineItemEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "routine_id") val routineId: String,
    @ColumnInfo(name = "exercise_id") val exerciseId: String,
    val position: Int,
    @ColumnInfo(name = "target_sets") val targetSets: Int,
    @ColumnInfo(name = "target_reps") val targetReps: Int?,
    @ColumnInfo(name = "target_weight_g") val targetWeightGrams: Long?,
    @ColumnInfo(name = "target_seconds") val targetSeconds: Int?,
    @ColumnInfo(name = "target_distance_m") val targetDistanceMetres: Int?,
    @ColumnInfo(name = "rest_seconds") val restSeconds: Int?,
)

/**
 * A session. Without times it is an amount on [day] (harvest FA10). [day] is the local date it
 * counts on, computed once by :core countingDay (FA13). state: OPEN (in progress, resumable),
 * FINISHED, STOPPED_EARLY (KA2). intensity 1-5 (GR4).
 */
@Entity(
    tableName = "session",
    foreignKeys = [ForeignKey(RoutineEntity::class, ["id"], ["routine_id"], onDelete = ForeignKey.SET_NULL)],
    indices = [Index("routine_id"), Index("day")],
)
data class SessionEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "routine_id") val routineId: String?,
    val activity: String?,
    @ColumnInfo(name = "started_at") val startedAt: Long?,
    @ColumnInfo(name = "ended_at") val endedAt: Long?,
    val zone: String,
    val day: String,
    val state: String,
    val intensity: Int?,
    val note: String?,
)

/**
 * A done set; skipped sets are never written (KA2). rpe is RPE x10 (BC1). side: L, R or null.
 * Schema 2: power is "move fast" (0.19); partial_reps is its own count beside reps, never converted
 * into reps (0.33: 7 full and 1 partial is reps 7, partial_reps 1).
 */
@Entity(
    tableName = "logged_set",
    foreignKeys = [
        ForeignKey(SessionEntity::class, ["id"], ["session_id"], onDelete = ForeignKey.CASCADE),
        ForeignKey(ExerciseEntity::class, ["id"], ["exercise_id"], onDelete = ForeignKey.RESTRICT),
    ],
    indices = [Index("session_id"), Index(value = ["exercise_id", "done_at"])],
)
data class LoggedSetEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "session_id") val sessionId: String,
    @ColumnInfo(name = "exercise_id") val exerciseId: String,
    val position: Int,
    val side: String?,
    val reps: Int?,
    @ColumnInfo(name = "weight_g") val weightGrams: Long?,
    val seconds: Int?,
    @ColumnInfo(name = "distance_m") val distanceMetres: Int?,
    val rpe: Int?,
    val rir: Int?,
    @ColumnInfo(name = "done_at") val doneAt: Long,
    @ColumnInfo(defaultValue = "0") val power: Boolean = false,
    @ColumnInfo(name = "partial_reps", defaultValue = "0") val partialReps: Int = 0,
)

/** LL5: add this after a session where every set hit its target. when: ALL_SETS_HIT. */
@Entity(
    tableName = "progression_rule",
    foreignKeys = [ForeignKey(RoutineItemEntity::class, ["id"], ["routine_item_id"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("routine_item_id", unique = true)],
)
data class ProgressionRuleEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "routine_item_id") val routineItemId: String,
    @ColumnInfo(name = "add_weight_g") val addWeightGrams: Long,
    @ColumnInfo(name = "add_reps") val addReps: Int,
    @ColumnInfo(name = "when") val whenRule: String,
)

/** TX1, FA1, SH1: a saved interval run; last_used_at orders the repeat-one-tap list. */
@Entity(tableName = "interval_preset")
data class IntervalPresetEntity(
    @PrimaryKey val id: String,
    val name: String,
    @ColumnInfo(name = "prepare_s") val prepareSeconds: Int,
    @ColumnInfo(name = "work_s") val workSeconds: Int,
    @ColumnInfo(name = "rest_s") val restSeconds: Int,
    val rounds: Int,
    val sets: Int,
    @ColumnInfo(name = "set_rest_s") val setRestSeconds: Int,
    @ColumnInfo(name = "last_used_at") val lastUsedAt: Long?,
)

/** OT1: one GPS fix. segment changes after a pause. */
@Entity(
    tableName = "track_point",
    foreignKeys = [ForeignKey(SessionEntity::class, ["id"], ["session_id"], onDelete = ForeignKey.CASCADE)],
    indices = [Index(value = ["session_id", "t"])],
)
data class TrackPointEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "session_id") val sessionId: String,
    val t: Long,
    @ColumnInfo(name = "lat_e7") val latE7: Int,
    @ColumnInfo(name = "lon_e7") val lonE7: Int,
    @ColumnInfo(name = "alt_dm") val altDecimetres: Int?,
    @ColumnInfo(name = "accuracy_m") val accuracyMetres: Int?,
    val segment: Int,
)

/** FA9: period DAY, WEEK, MONTH; unit SESSIONS, MINUTES, METRES, SETS; exercise null = all training. */
@Entity(
    tableName = "goal",
    foreignKeys = [ForeignKey(ExerciseEntity::class, ["id"], ["exercise_id"], onDelete = ForeignKey.CASCADE)],
    indices = [Index("exercise_id")],
)
data class GoalEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "exercise_id") val exerciseId: String?,
    val period: String,
    val target: Long,
    val unit: String,
)

/** OS8 and later preferences; key is unique. */
@Entity(tableName = "setting", indices = [Index("key", unique = true)])
data class SettingEntity(
    @PrimaryKey val id: String,
    val key: String,
    val value: String,
)

/** Schema 2 (0.23): a label from the person's reusable list; archived hides it from the list. */
@Entity(tableName = "label")
data class LabelEntity(
    @PrimaryKey val id: String,
    val name: String,
    val position: Int,
    val archived: Boolean,
)

/** Schema 2 (0.23): a label on a session, several per session. */
@Entity(
    tableName = "session_label",
    foreignKeys = [
        ForeignKey(SessionEntity::class, ["id"], ["session_id"], onDelete = ForeignKey.CASCADE),
        ForeignKey(LabelEntity::class, ["id"], ["label_id"], onDelete = ForeignKey.CASCADE),
    ],
    indices = [Index(value = ["session_id", "label_id"], unique = true), Index("label_id")],
)
data class SessionLabelEntity(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "session_id") val sessionId: String,
    @ColumnInfo(name = "label_id") val labelId: String,
)

/**
 * Schema 2, the body-measures module (OS1, 0.10): the person's own measure, an integer [value] in
 * the stated [unit] (G for WEIGHT, MM for a girth such as WAIST), so a new kind needs no migration.
 * [day] follows the session rule: the local date of taken_at in zone. No BMI, BMR or forecast is
 * stored or derived.
 */
@Entity(tableName = "body_measure", indices = [Index(value = ["kind", "taken_at"]), Index("day")])
data class BodyMeasureEntity(
    @PrimaryKey val id: String,
    val kind: String,
    val value: Long,
    val unit: String,
    @ColumnInfo(name = "taken_at") val takenAt: Long,
    val zone: String,
    val day: String,
    val note: String?,
)

/**
 * Every table in dependency order: a parent before its children. Restore inserts in this order and
 * deletes in reverse; a table missing here would be missing from every backup, so a test pins this
 * list to sqlite_master.
 */
val TABLES = listOf(
    "exercise", "routine", "routine_item", "session", "logged_set",
    "progression_rule", "interval_preset", "track_point", "goal", "setting",
    // Schema 2, appended so a schema-1 backup's tables keep their place.
    "exercise_module", "label", "session_label", "body_measure",
)
