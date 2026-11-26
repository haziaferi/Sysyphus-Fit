package com.hazia.sisyphusfit.data

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import kotlinx.coroutines.test.runTest
import java.io.File
import java.nio.file.Files
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/*
 * Every migration is additive (SPEC:R33), so every cell of an older file must come out exactly as it went in. The older
 * files come from OldSchemas.kt (jvmTestShared), shared with the Robolectric test.
 */

private val driver = BundledSQLiteDriver()
private val dirs = mutableListOf<File>()
private val OLD_SCHEMAS = listOf(1, 2)

private fun tempFile(): File = File(Files.createTempDirectory("sisyphus").toFile().also { dirs += it }, DATABASE_NAME)

private fun setAside(file: File) = file.parentFile!!.list()!!.filter { UNOPENABLE_SUFFIX in it }

class MigrationTest {
    @AfterTest fun clean() = dirs.forEach { it.deleteRecursively() }

    @Test
    fun everyOlderRowSurvivesByteForByteAndTheNewPartsStartAtTheirDefaults() {
        for (version in OLD_SCHEMAS) {
            val file = tempFile()
            val oldColumns = createSchema(version, file, driver)
            val before = raw(file, driver) { it.allCells(oldColumns) }
            assertEquals(if (version == 1) schema1Rows.size else schema2Rows.size, before.values.sumOf { it.size })

            val open = openSisyphusDatabase(file)
            open.database.close()
            assertFalse(open.recovered)
            assertEquals(emptyList(), setAside(file))

            raw(file, driver) { c ->
                assertEquals(before, c.allCells(oldColumns), "schema $version")
                assertMigrated(c, oldColumns, from = version)
            }
        }
    }

    @Test
    fun aMigratedDatabaseHasExactlyTheShapeOfAFreshOne() {
        val fresh = tempFile().also { openSisyphusDatabase(it).database.close() }
        val b = raw(fresh, driver) { c -> TABLES.associateWith { c.shape(it) } }
        for (version in OLD_SCHEMAS) {
            val migrated = tempFile().also { createSchema(version, it, driver) }
            openSisyphusDatabase(migrated).database.close()
            val a = raw(migrated, driver) { c -> TABLES.associateWith { c.shape(it) } }
            TABLES.forEach { assertEquals(b[it], a[it], "$it from schema $version") }
        }
    }

    @Test
    fun aMigratedDatabaseTakesTheNewRowsAndPassesTheBackupGate() = runTest {
        val file = tempFile().also { createSchema(1, it, driver) }
        val db = openSisyphusDatabase(file).database
        val f = db.floorDao()
        f.addExerciseModules(listOf(ExerciseModuleEntity("M1", "E1", "STRENGTH"), ExerciseModuleEntity("M2", "E1", "MOBILITY_BALANCE")))
        f.addLabel(LabelEntity("B1", "Casa", position = 0, archived = false))
        f.addSessionLabels(listOf(SessionLabelEntity("SL1", "S1", "B1")))
        f.addBodyMeasure(BodyMeasureEntity("BM1", "WAIST", 812, "MM", 1_000, "Europe/Rome", "2026-10-19", "mattina"))
        f.addSets(listOf(LoggedSetEntity("L4", "S1", "E1", 2, null, 7, 60_000, null, null, null, null, 1_300, power = true, partialReps = 1, kind = "DYNAMIC")))
        f.addSession(SessionEntity("S4", null, "Walk", null, null, "Europe/Rome", "2026-10-21", "FINISHED", null, null, seconds = 1_800, module = "DAILY_MOVEMENT"))
        f.addSteps(StepsEntity("ST1", "2026-10-21", 6_500, "ENTERED"))
        f.addCategory(CategoryEntity("C1", "core"))
        f.addExerciseCategories(listOf(ExerciseCategoryEntity("EC1", "E2", "C1")))

        val first = exportBackup(db)
        val other = openSisyphusDatabase(tempFile()).database
        restoreBackup(other, first)
        assertEquals(first, exportBackup(other))
        listOf("session_label", "steps", "exercise_category").forEach { assertTrue("\"$it\": [\n" in first, it) }
        db.close(); other.close()
    }

    /**
     * SPEC:R35, the database never deletes itself, applied to migrations: a migration that fails stops the open, and the
     * file is left exactly as it was (its version, every table, every cell), not set aside and not wiped, so a fixed (or
     * newer) app can open it on the next start.
     */
    @Test
    fun aFailedMigrationLeavesTheFileAsItWas() {
        for (version in OLD_SCHEMAS) {
            for ((how, damage) in migrationBreakers(version)) {
                val label = "schema $version, $how"
                val file = tempFile()
                val oldColumns = createSchema(version, file, driver, damage)
                val before = raw(file, driver) { it.untouchable(oldColumns) }

                assertTrue(runCatching { openSisyphusDatabase(file) }.isFailure, "$label: the open should fail")

                assertTrue(file.exists(), label)
                assertEquals(emptyList(), setAside(file), label)
                assertEquals(before, raw(file, driver) { it.untouchable(oldColumns) }, label)
            }
        }
    }
}
