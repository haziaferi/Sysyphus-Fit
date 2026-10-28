package com.hazia.sisyphusfit.data

import com.hazia.sisyphusfit.core.Module
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/** Exercises with their module tags (0.23): several per exercise, replaced whole. */
class ExercisesTest {
    private val temp = TempDatabase()
    private val db = temp.db
    private val store = ExerciseStore(db = { db }, clock = TestClock().clock)

    @AfterTest fun close() = temp.close()

    @Test fun addedExerciseCarriesItsTags() = runBlocking {
        val id = store.add("  Goblet squat ", ExerciseKind.DYNAMIC, sided = false, setOf(Module.STRENGTH, Module.MOBILITY_BALANCE))
        val expected = Exercise(id, "Goblet squat", ExerciseKind.DYNAMIC, false, false, setOf(Module.STRENGTH, Module.MOBILITY_BALANCE))
        assertEquals<List<Exercise>>(listOf(expected), store.active().first())
    }

    @Test fun blankNameIsRefused() {
        assertFailsWith<IllegalArgumentException> { runBlocking { store.add("   ", ExerciseKind.DYNAMIC, false, emptySet()) } }
    }

    @Test fun updateReplacesTagsWhole() = runBlocking {
        val id = store.add("Row", ExerciseKind.DYNAMIC, false, setOf(Module.STRENGTH))
        val before = store.active().first().single()
        store.update(before.copy(name = "Cable row", sided = true, showPartialReps = true, modules = setOf(Module.STAMINA)))
        assertEquals(Exercise(id, "Cable row", ExerciseKind.DYNAMIC, true, true, setOf(Module.STAMINA)), store.active().first().single())
        assertEquals(1L, db.count("SELECT count(*) FROM exercise_module"))
    }

    @Test fun archivedLeavesTheListButKeepsItsSets() = runBlocking {
        val id = store.add("Dip", ExerciseKind.DYNAMIC, false, setOf(Module.STRENGTH))
        db.floorDao().addSession(SessionEntity("S1", null, null, 1_000, 2_000, "Europe/Rome", "2026-10-28", "FINISHED", null, null))
        db.floorDao().addSets(listOf(LoggedSetEntity("L1", "S1", id, 0, null, 8, null, null, null, null, null, 1_500)))
        store.archive(id)
        assertTrue(store.active().first().isEmpty())
        assertEquals(1L, db.count("SELECT count(*) FROM logged_set"))
    }

    @Test fun listIsByNameIgnoringCase() = runBlocking {
        listOf("bench", "Apple", "curl").forEach { store.add(it, ExerciseKind.DYNAMIC, false, emptySet()) }
        assertEquals(listOf("Apple", "bench", "curl"), store.active().first().map { it.name })
    }

    // A backup from a newer build may tag an exercise with a module this one does not know.
    @Test fun unknownModuleTagIsSkipped() = runBlocking {
        val id = store.add("Plank", ExerciseKind.ISOMETRIC, false, setOf(Module.STRENGTH))
        db.floorDao().addExerciseModules(listOf(ExerciseModuleEntity("X", id, "JUGGLING")))
        assertEquals(setOf(Module.STRENGTH), store.active().first().single().modules)
    }

    // Review I1: a kind a newer build wrote must not take the whole list down; that exercise is left out.
    @Test fun unknownKindLeavesThatExerciseOut() = runBlocking {
        store.add("Squat", ExerciseKind.DYNAMIC, false, setOf(Module.STRENGTH))
        db.floorDao().addExercise(ExerciseEntity("X", "Assisted pull-up", "ASSISTED", sided = false, archived = false))
        assertEquals(listOf("Squat"), store.active().first().map { it.name })
    }
}
