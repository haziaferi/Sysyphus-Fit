package com.hazia.sisyphusfit.data

import androidx.room.useReaderConnection
import androidx.room.useWriterConnection
import kotlinx.coroutines.test.runTest
import java.io.File
import java.nio.file.Files
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

private val dirs = mutableListOf<File>()

private fun freshDb(): SisyphusDatabase {
    val dir = Files.createTempDirectory("sisyphus").toFile().also { dirs += it }
    return openSisyphusDatabase(File(dir, DATABASE_NAME)).database
}

/** One row in every table, with nulls where a column allows them, so the round trip sees both. */
private suspend fun seed(db: SisyphusDatabase) {
    val f = db.floorDao()
    f.addExercise(ExerciseEntity("E1", "Bench press", "DYNAMIC", sided = false, archived = false))
    f.addExercise(ExerciseEntity("E2", "Side plank", "ISOMETRIC", sided = true, archived = false))
    f.addRoutine(RoutineEntity("R1", "Monday", weekdays = 0b0000001, position = 0))
    f.addRoutineItem(RoutineItemEntity("RI1", "R1", "E1", 0, 3, 5, 60_000, null, null, 90))
    f.addSession(SessionEntity("S1", "R1", null, 1_000, 2_000, "Europe/Rome", "2026-10-19", "FINISHED", 3, "felt good"))
    f.addSession(SessionEntity("S2", null, "Walk", null, null, "Europe/Rome", "2026-10-19", "FINISHED", null, null))
    f.addSets(
        listOf(
            LoggedSetEntity("L1", "S1", "E1", 0, null, 5, 60_000, null, null, 80, 2, 1_100),
            LoggedSetEntity("L2", "S1", "E2", 1, "L", null, null, 30, null, null, null, 1_200),
        ),
    )
    db.useWriterConnectionExec(
        "INSERT INTO progression_rule VALUES ('P1', 'RI1', 2500, 0, 'ALL_SETS_HIT')",
        "INSERT INTO interval_preset VALUES ('I1', 'Tabata', 10, 20, 10, 8, 1, 0, NULL)",
        "INSERT INTO track_point VALUES ('T1', 'S2', 1500, 419028000, 124964000, NULL, 5, 0)",
        "INSERT INTO goal VALUES ('G1', NULL, 'WEEK', 4, 'SESSIONS')",
        "INSERT INTO setting VALUES ('K1', 'backup.folder', 'content://x')",
    )
}

private suspend fun SisyphusDatabase.useWriterConnectionExec(vararg sql: String) =
    useWriterConnection { c -> sql.forEach { q -> c.usePrepared(q) { it.step() } } }

class SchemaTest {
    @AfterTest fun clean() = dirs.forEach { it.deleteRecursively() }

    @Test
    fun theBackupListNamesEveryTableInTheDatabase() = runTest {
        val db = freshDb()
        val inDb = db.useReaderConnection { c ->
            c.usePrepared(
                "SELECT name FROM sqlite_master WHERE type = 'table' AND name NOT IN " +
                    "('room_master_table', 'android_metadata', 'sqlite_sequence')",
            ) { s -> buildSet { while (s.step()) add(s.getText(0)) } }
        }
        assertEquals(inDb, TABLES.toSet())
        db.close()
    }

    @Test
    fun foreignKeysAreEnforced() = runTest {
        val db = freshDb()
        assertFails { db.floorDao().addSets(listOf(LoggedSetEntity("L9", "nope", "nope", 0, null, 1, null, null, null, null, null, 0))) }
        db.close()
    }
}

private suspend fun assertFails(block: suspend () -> Unit) {
    val failed = runCatching { block() }.isFailure
    assertTrue(failed, "expected a failure")
}

class BackupTest {
    @AfterTest fun clean() = dirs.forEach { it.deleteRecursively() }

    @Test
    fun exportRestoreExportIsByteIdentical() = runTest {
        val a = freshDb().also { seed(it) }
        val first = exportBackup(a)
        val b = freshDb()
        restoreBackup(b, first)
        assertEquals(first, exportBackup(b))
        assertEquals(listOf("L1", "L2").size, b.floorDao().lastSets("E1").size + b.floorDao().lastSets("E2").size)
        a.close(); b.close()
    }

    @Test
    fun theBackupHoldsEveryTableAndItsNulls() = runTest {
        val db = freshDb().also { seed(it) }
        val text = exportBackup(db)
        TABLES.forEach { assertTrue("\"$it\"" in text, "table $it missing") }
        assertTrue("\"note\": null" in text)
        db.close()
    }

    @Test
    fun restoreReplacesWhatWasThere() = runTest {
        val db = freshDb().also { seed(it) }
        val empty = exportBackup(freshDb())
        restoreBackup(db, empty)
        assertEquals(empty, exportBackup(db))
        db.close()
    }

    @Test
    fun aRefusedRestoreChangesNothing() = runTest {
        val db = freshDb().also { seed(it) }
        val before = exportBackup(db)
        // Fails on the LAST table, after every DELETE has run: the transaction must undo them.
        val bad = before.replace("\"key\": \"backup.folder\"", "\"no_such_column\": \"x\"")
        assertNotEquals(before, bad)
        assertFailsWith<BackupException> { restoreBackup(db, bad) }
        assertEquals(before, exportBackup(db))

        for (wrong in listOf("{}", "not json", before.replace(BACKUP_FORMAT, "other"),
            before.replace("\"version\": 1", "\"version\": 99"), before.replace("\"exercise\":", "\"exercize\":"))) {
            assertFailsWith<BackupException>(wrong.take(40)) { restoreBackup(db, wrong) }
            assertEquals(before, exportBackup(db))
        }
        db.close()
    }
}

class FloorTest {
    @AfterTest fun clean() = dirs.forEach { it.deleteRecursively() }

    @Test
    fun lastSetsComeFromTheLastClosedSessionAndSkipAnOpenOne() = runTest {
        val db = freshDb().also { seed(it) }
        val f = db.floorDao()
        f.addSession(SessionEntity("S3", "R1", null, 5_000, null, "Europe/Rome", "2026-10-20", "OPEN", null, null))
        f.addSets(listOf(LoggedSetEntity("L3", "S3", "E1", 0, null, 3, 62_500, null, null, null, null, 5_100)))
        assertEquals(listOf("L1"), f.lastSets("E1").map { it.id })
        assertEquals("S3", f.openSession()?.id)
        db.close()
    }

    @Test
    fun noOpenSessionIsNull() = runTest {
        val db = freshDb().also { seed(it) }
        assertNull(db.floorDao().openSession())
        db.close()
    }
}

class CorruptionTest {
    @AfterTest fun clean() = dirs.forEach { it.deleteRecursively() }

    @Test
    fun aCorruptFileIsSetAsideWithItsBytesIntact() {
        val dir = Files.createTempDirectory("sisyphus").toFile().also { dirs += it }
        val garbage = "this is not a database\n".toByteArray()
        val file = File(dir, DATABASE_NAME).apply { writeBytes(garbage) }

        val open = openSisyphusDatabase(file) { 42 }
        open.database.close()

        assertTrue(open.recovered)
        assertContentEquals(garbage, File(file.path + UNOPENABLE_SUFFIX + 42).readBytes())
        assertTrue(file.exists())
    }

    @Test
    fun aHealthyFileIsNotRecovered() {
        val dir = Files.createTempDirectory("sisyphus").toFile().also { dirs += it }
        val first = openSisyphusDatabase(File(dir, DATABASE_NAME))
        first.database.close()
        val again = openSisyphusDatabase(File(dir, DATABASE_NAME))
        again.database.close()
        assertFalse(again.recovered)
        assertEquals(0, dir.listFiles()!!.count { UNOPENABLE_SUFFIX in it.name })
    }
}
