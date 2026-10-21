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
 * Schema 1 -> 2 (SPEC §4): additive, so every schema-1 cell must come out of the migration exactly as
 * it went in. The schema-1 file comes from Schema1.kt (jvmTestShared), shared with the Robolectric test.
 */

private val driver = BundledSQLiteDriver()
private val dirs = mutableListOf<File>()

private fun tempFile(): File = File(Files.createTempDirectory("sisyphus").toFile().also { dirs += it }, DATABASE_NAME)

private fun setAside(file: File) = file.parentFile!!.list()!!.filter { UNOPENABLE_SUFFIX in it }

class MigrationTest {
    @AfterTest fun clean() = dirs.forEach { it.deleteRecursively() }

    @Test
    fun everySchema1RowSurvivesByteForByteAndTheNewPartsStartAtTheirDefaults() {
        val file = tempFile()
        val v1Columns = createSchema1(file, driver)
        val before = raw(file, driver) { it.allCells(v1Columns) }
        assertEquals(schema1Rows.size, before.values.sumOf { it.size })

        val open = openSisyphusDatabase(file)
        open.database.close()
        assertFalse(open.recovered)
        assertEquals(emptyList(), setAside(file))

        raw(file, driver) { c ->
            assertEquals(before, c.allCells(v1Columns))
            assertMigrated(c, v1Columns)
        }
    }

    @Test
    fun aMigratedDatabaseHasExactlyTheShapeOfAFreshOne() {
        val migrated = tempFile().also { createSchema1(it, driver) }
        val fresh = tempFile()
        listOf(migrated, fresh).forEach { openSisyphusDatabase(it).database.close() }
        val a = raw(migrated, driver) { c -> TABLES.associateWith { c.shape(it) } }
        val b = raw(fresh, driver) { c -> TABLES.associateWith { c.shape(it) } }
        TABLES.forEach { assertEquals(b[it], a[it], it) }
    }

    @Test
    fun aMigratedDatabaseTakesTheNewRowsAndPassesTheBackupGate() = runTest {
        val file = tempFile().also { createSchema1(it, driver) }
        val db = openSisyphusDatabase(file).database
        val f = db.floorDao()
        f.addExerciseModules(listOf(ExerciseModuleEntity("M1", "E1", "STRENGTH"), ExerciseModuleEntity("M2", "E1", "MOBILITY_BALANCE")))
        f.addLabel(LabelEntity("B1", "Casa", position = 0, archived = false))
        f.addSessionLabels(listOf(SessionLabelEntity("SL1", "S1", "B1")))
        f.addBodyMeasure(BodyMeasureEntity("BM1", "WAIST", 812, "MM", 1_000, "Europe/Rome", "2026-10-19", "mattina"))
        f.addSets(listOf(LoggedSetEntity("L4", "S1", "E1", 2, null, 7, 60_000, null, null, null, null, 1_300, power = true, partialReps = 1)))

        val first = exportBackup(db)
        val other = openSisyphusDatabase(tempFile()).database
        restoreBackup(other, first)
        assertEquals(first, exportBackup(other))
        assertTrue("\"session_label\": [\n" in first)
        db.close(); other.close()
    }

    /**
     * SPEC §5, the database never deletes itself, applied to migrations: a migration that fails stops
     * the open, and the file is left exactly as it was (its version, every table, every cell), not set
     * aside and not wiped, so a fixed (or newer) app can open it on the next start.
     */
    @Test
    fun aFailedMigrationLeavesTheFileAsItWas() {
        for ((how, damage) in migrationBreakers) {
            val file = tempFile()
            val v1Columns = createSchema1(file, driver, damage)
            val before = raw(file, driver) { it.untouchable(v1Columns) }

            assertTrue(runCatching { openSisyphusDatabase(file) }.isFailure, "$how: the open should fail")

            assertTrue(file.exists(), how)
            assertEquals(emptyList(), setAside(file), how)
            assertEquals(before, raw(file, driver) { it.untouchable(v1Columns) }, how)
        }
    }
}
