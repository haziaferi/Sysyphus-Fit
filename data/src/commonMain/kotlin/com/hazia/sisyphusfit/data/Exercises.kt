package com.hazia.sisyphusfit.data

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import androidx.room.Update
import com.hazia.sisyphusfit.core.Ids
import com.hazia.sisyphusfit.core.Module
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/** SPEC §4: how an exercise is counted. */
enum class ExerciseKind { DYNAMIC, ISOMETRIC, TIMED, DISTANCE }

/** An exercise as the screens see it: its row and its module tags (0.23). */
data class Exercise(
    val id: String,
    val name: String,
    val kind: ExerciseKind,
    val sided: Boolean,
    val showPartialReps: Boolean,
    val modules: Set<Module>,
)

data class ExerciseWithModules(
    @Embedded val exercise: ExerciseEntity,
    @Relation(parentColumn = "id", entityColumn = "exercise_id", entity = ExerciseModuleEntity::class, projection = ["module"])
    val modules: List<String>,
)

@Dao
abstract class ExerciseDao {
    @Transaction
    @Query("SELECT * FROM exercise WHERE archived = 0 ORDER BY name COLLATE NOCASE, id")
    abstract fun active(): Flow<List<ExerciseWithModules>>

    @Insert protected abstract suspend fun insert(row: ExerciseEntity)
    @Update protected abstract suspend fun update(row: ExerciseEntity)
    @Insert protected abstract suspend fun insertModules(rows: List<ExerciseModuleEntity>)

    @Query("DELETE FROM exercise_module WHERE exercise_id = :exerciseId")
    protected abstract suspend fun clearModules(exerciseId: String)

    @Query("SELECT * FROM exercise WHERE id = :id")
    abstract suspend fun byId(id: String): ExerciseEntity?

    /** Archived exercises leave the list; their logged sets stay. */
    @Query("UPDATE exercise SET archived = 1 WHERE id = :id")
    abstract suspend fun archive(id: String)

    /** Writes [row] and replaces its tags whole, in one transaction. */
    @Transaction
    open suspend fun save(row: ExerciseEntity, modules: List<ExerciseModuleEntity>, isNew: Boolean) {
        if (isNew) insert(row) else update(row)
        clearModules(row.id)
        insertModules(modules)
    }
}

class ExerciseStore(private val db: Db, private val clock: WallClock) {
    fun active(): Flow<List<Exercise>> = flow { emitAll(db().exerciseDao().active()) }.map { rows -> rows.map(::toExercise) }

    suspend fun add(name: String, kind: ExerciseKind, sided: Boolean, modules: Set<Module>): String {
        val id = Ids.ulid(clock.now())
        val row = ExerciseEntity(id, cleanName(name), kind.name, sided, archived = false)
        db().exerciseDao().save(row, tags(id, modules), isNew = true)
        return id
    }

    /** Saves the exercise's name, kind, switches and tags; an archived exercise stays archived. */
    suspend fun update(exercise: Exercise) {
        val dao = db().exerciseDao()
        val current = checkNotNull(dao.byId(exercise.id)) { "no exercise ${exercise.id}" }
        val row = current.copy(
            name = cleanName(exercise.name),
            kind = exercise.kind.name,
            sided = exercise.sided,
            showPartialReps = exercise.showPartialReps,
        )
        dao.save(row, tags(exercise.id, exercise.modules), isNew = false)
    }

    suspend fun archive(id: String) = db().exerciseDao().archive(id)

    private fun cleanName(name: String): String = name.trim().also { require(it.isNotEmpty()) { "an exercise needs a name" } }

    private fun tags(exerciseId: String, modules: Set<Module>) =
        Module.entries.filter { it in modules }.map { ExerciseModuleEntity(Ids.ulid(clock.now()), exerciseId, it.name) }

    // A tag this build does not know (a backup from a newer one) is skipped.
    private fun toExercise(row: ExerciseWithModules) = Exercise(
        id = row.exercise.id,
        name = row.exercise.name,
        kind = ExerciseKind.valueOf(row.exercise.kind),
        sided = row.exercise.sided,
        showPartialReps = row.exercise.showPartialReps,
        modules = row.modules.mapNotNull { name -> Module.entries.firstOrNull { it.name == name } }.toSet(),
    )
}
