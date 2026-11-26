package com.hazia.sisyphusfit.data

import androidx.room.useReaderConnection
import androidx.room.useWriterConnection
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
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
    f.addExercise(ExerciseEntity("E2", "Side plank", "ISOMETRIC", sided = true, archived = false, showPartialReps = true, restSeconds = 0, note = "hips up"))
    f.addExercise(ExerciseEntity("E3", "Row", "DYNAMIC", sided = false, archived = false, restSeconds = 90))
    f.addExerciseModules(
        listOf(
            ExerciseModuleEntity("M1", "E1", "STRENGTH"),
            ExerciseModuleEntity("M2", "E2", "STRENGTH"),
            ExerciseModuleEntity("M3", "E2", "MOBILITY_BALANCE"),
        ),
    )
    f.addRoutine(RoutineEntity("R1", "Monday", weekdays = 0b0000001, position = 0))
    f.addRoutineItem(RoutineItemEntity("RI1", "R1", "E1", 0, 3, 5, 60_000, null, null, 90))
    f.addSession(SessionEntity("S1", "R1", null, 1_000, 2_000, "Europe/Rome", "2026-10-19", "FINISHED", 3, "felt good"))
    f.addSession(SessionEntity("S2", null, "Walk", null, null, "Europe/Rome", "2026-10-19", "FINISHED", null, null, seconds = 1_800, module = "DAILY_MOVEMENT"))
    f.addSets(
        listOf(
            LoggedSetEntity("L1", "S1", "E1", 0, null, 5, 60_000, null, null, 80, 2, 1_100, kind = "DYNAMIC"),
            LoggedSetEntity("L2", "S1", "E2", 1, "L", null, null, 30, null, null, null, 1_200, kind = "ISOMETRIC"),
            // Logged before E1's type was changed: the set keeps its own.
            LoggedSetEntity("L3", "S1", "E1", 2, null, 7, 40_000, null, null, null, null, 1_300, power = true, partialReps = 1, kind = "TIMED"),
        ),
    )
    f.addSteps(StepsEntity("ST1", "2026-10-19", 6_500, "ENTERED"))
    f.addSteps(StepsEntity("ST2", "2026-10-19", 7_012, "SENSOR"))
    f.addCategory(CategoryEntity("C1", "core"))
    f.addExerciseCategories(listOf(ExerciseCategoryEntity("EC1", "E2", "C1")))
    f.addLabel(LabelEntity("B1", "Gym", position = 0, archived = false))
    f.addSessionLabels(listOf(SessionLabelEntity("SL1", "S1", "B1")))
    f.addBodyMeasure(BodyMeasureEntity("BM1", "WEIGHT", 71_250, "G", 900, "Europe/Rome", "2026-10-19", null))
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
        assertFails { db.floorDao().addSets(listOf(LoggedSetEntity("L9", "nope", "nope", 0, null, 1, null, null, null, null, null, 0, kind = "DYNAMIC"))) }
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
        assertEquals(listOf("L1") to listOf("L2"), b.floorDao().lastSets("E1").map { it.id } to b.floorDao().lastSets("E2").map { it.id })
        a.close(); b.close()
    }

    @Test
    fun theBackupHoldsEveryTableAndItsNulls() = runTest {
        val db = freshDb().also { seed(it) }
        val text = exportBackup(db)
        TABLES.forEach { assertTrue("\"$it\": [\n" in text, "table $it missing or empty") }
        assertTrue("\"note\": null" in text)
        assertEquals(3, BACKUP_VERSION)
        assertTrue("\"version\": 3," in text)
        // SPEC:R32: a partial rep is its own count, never folded into reps.
        assertTrue("\"reps\": 7,\n" in text && "\"power\": 1,\n" in text && "\"partial_reps\": 1,\n" in text)
        // SPEC:R121: a set's own type, an amount's time and module; SPEC:R103: no rest (0) differs from the default (null).
        assertTrue("\"kind\": \"TIMED\"\n" in text && "\"seconds\": 1800,\n" in text && "\"module\": \"DAILY_MOVEMENT\"" in text)
        assertTrue("\"rest_seconds\": 0,\n" in text && "\"rest_seconds\": null,\n" in text)
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
            before.replace("\"version\": 3", "\"version\": 99"), before.replace("\"exercise\":", "\"exercize\":"))) {
            assertFailsWith<BackupException>(wrong.take(40)) { restoreBackup(db, wrong) }
            assertEquals(before, exportBackup(db))
        }
        db.close()
    }
}

/**
 * Backups written by older apps, each its own export of its seed: version 1 by the schema-1 app (c1bed2a), version 2 by
 * the schema-2 app (acba13a).
 */
private fun oldBackup(version: Int): String = BackupTest::class.java.getResource("/backup-v$version.json")!!.readText()

/** The columns each later version added, with the value a restored older row takes: "0", null, or its exercise's type. */
private val addedColumns = mapOf(
    2 to mapOf("exercise" to mapOf("show_partial_reps" to "0"), "logged_set" to mapOf("power" to "0", "partial_reps" to "0")),
    3 to mapOf(
        "exercise" to mapOf("rest_seconds" to null, "note" to null),
        "session" to mapOf("seconds" to null, "module" to null),
        "logged_set" to mapOf("kind" to "its exercise's type"),
    ),
)

class OldBackupTest {
    @AfterTest fun clean() = dirs.forEach { it.deleteRecursively() }

    // SPEC:R36, SPEC:R121: every row comes back unchanged, the newer columns at their defaults, each set under its
    // exercise's type, and the newer tables empty.
    @Test
    fun anOlderBackupRestoresEveryRowWithTheNewColumnsAtTheirDefaults() = runTest {
        for (version in listOf(1, 2)) {
            val file = Json.parseToJsonElement(oldBackup(version)).jsonObject
            assertEquals(version, file["version"]!!.jsonPrimitive.int)
            val db = freshDb()
            restoreBackup(db, oldBackup(version))
            val now = Json.parseToJsonElement(exportBackup(db)).jsonObject["tables"]!!.jsonObject

            val old = file["tables"]!!.jsonObject
            val kindOf = old.getValue("exercise").jsonArray.associate { e ->
                e.jsonObject.getValue("id").jsonPrimitive.content to e.jsonObject.getValue("kind")
            }
            val added = addedColumns.filterKeys { it > version }.values
            for ((table, rows) in old) {
                val restored = now.getValue(table).jsonArray.map { it.jsonObject }
                assertEquals(rows.jsonArray.size, restored.size, "v$version $table")
                rows.jsonArray.zip(restored).forEach { (was, isNow) ->
                    val label = "v$version $table ${isNow.getValue("id")}"
                    assertEquals(was.jsonObject, JsonObject(isNow.filterKeys { it in was.jsonObject }), "$label changed")
                    val expected = added.mapNotNull { it[table] }.fold(emptyMap<String, String?>()) { a, b -> a + b }
                    assertEquals(expected.keys, isNow.keys - was.jsonObject.keys, "$label: added columns")
                    for ((col, value) in expected) {
                        val want = when (value) {
                            null -> JsonNull
                            "0" -> JsonPrimitive(0)
                            else -> kindOf.getValue(isNow.getValue("exercise_id").jsonPrimitive.content)
                        }
                        assertEquals(want, isNow.getValue(col), "$label.$col")
                    }
                }
            }
            (TABLES - old.keys).forEach { assertEquals(0, now.getValue(it).jsonArray.size, "v$version $it should be empty") }
            db.close()
        }
    }
}

class FloorTest {
    @AfterTest fun clean() = dirs.forEach { it.deleteRecursively() }

    @Test
    fun lastSetsComeFromTheLastClosedSessionAndSkipAnOpenOne() = runTest {
        val db = freshDb().also { seed(it) }
        val f = db.floorDao()
        f.addSession(SessionEntity("S3", "R1", null, 5_000, null, "Europe/Rome", "2026-10-20", "OPEN", null, null))
        f.addSets(listOf(LoggedSetEntity("L9", "S3", "E1", 0, null, 3, 62_500, null, null, null, null, 5_100, kind = "DYNAMIC")))
        // L3 was logged under another type, so it is not last time (SPEC:R67).
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

    // SPEC:R122: the file set aside is the lasting mark that the person is owed the notice; the newest one counts.
    @Test
    fun theNewestSetAsideFileIsFound() {
        val dir = Files.createTempDirectory("sisyphus").toFile().also { dirs += it }
        val file = File(dir, DATABASE_NAME)
        assertEquals(null, newestSetAside(file.path))
        File(file.path + UNOPENABLE_SUFFIX + 7).writeText("old")
        File(file.path + "-wal" + UNOPENABLE_SUFFIX + 99).writeText("a journal, not the database")
        File(file.path + UNOPENABLE_SUFFIX + 42).writeText("newer")
        assertEquals(42L, newestSetAside(file.path))
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
