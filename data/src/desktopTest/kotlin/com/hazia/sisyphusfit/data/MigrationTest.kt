package com.hazia.sisyphusfit.data

import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.sqlite.execSQL
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File
import java.nio.file.Files
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/*
 * Schema 1 -> 2 (SPEC §4): additive, so every schema-1 cell must come out of the migration exactly as
 * it went in. The schema-1 file is built from Room's own exported 1.json, not from today's entities.
 */

private val schema1Json = File("schemas/com.hazia.sisyphusfit.data.SisyphusDatabase/1.json")

/** Every schema-1 table filled, with nulls, non-ASCII text, a newline and extreme integers. */
private val schema1Rows = listOf(
    "INSERT INTO exercise VALUES ('E1', 'Panca piana — pausa 2″', 'DYNAMIC', 0, 0)",
    "INSERT INTO exercise VALUES ('E2', 'Side plank 🧘', 'ISOMETRIC', 1, 1)",
    "INSERT INTO routine VALUES ('R1', 'Lunedì', 1, 0)",
    "INSERT INTO routine VALUES ('R2', '', 127, 1)",
    "INSERT INTO routine_item VALUES ('RI1', 'R1', 'E1', 0, 3, 5, 60000, NULL, NULL, 90)",
    "INSERT INTO routine_item VALUES ('RI2', 'R1', 'E2', 1, 2, NULL, NULL, 45, NULL, NULL)",
    "INSERT INTO session VALUES ('S1', 'R1', NULL, 1000, 2000, 'Europe/Rome', '2026-10-19', 'FINISHED', 3, 'felt good')",
    "INSERT INTO session VALUES ('S2', NULL, 'Walk', NULL, NULL, 'Europe/Rome', '2026-10-19', 'FINISHED', NULL, NULL)",
    "INSERT INTO session VALUES ('S3', 'R1', NULL, 9223372036854775807, NULL, 'Asia/Tokyo', '2026-10-20', 'OPEN', NULL, 'a' || char(10) || 'b')",
    "INSERT INTO logged_set VALUES ('L1', 'S1', 'E1', 0, NULL, 5, 60000, NULL, NULL, 80, 2, 1100)",
    "INSERT INTO logged_set VALUES ('L2', 'S1', 'E2', 1, 'L', NULL, NULL, 30, NULL, NULL, NULL, 1200)",
    "INSERT INTO logged_set VALUES ('L3', 'S3', 'E1', 0, NULL, 0, -9223372036854775808, NULL, 0, NULL, NULL, 0)",
    "INSERT INTO progression_rule VALUES ('P1', 'RI1', 2500, 0, 'ALL_SETS_HIT')",
    "INSERT INTO interval_preset VALUES ('I1', 'Tabata', 10, 20, 10, 8, 1, 0, NULL)",
    "INSERT INTO track_point VALUES ('T1', 'S2', 1500, 419028000, 124964000, NULL, 5, 0)",
    "INSERT INTO track_point VALUES ('T2', 'S2', 1600, -899999999, -1799999999, -4300, NULL, 1)",
    "INSERT INTO goal VALUES ('G1', NULL, 'WEEK', 4, 'SESSIONS')",
    "INSERT INTO goal VALUES ('G2', 'E1', 'MONTH', 12, 'SETS')",
    "INSERT INTO setting VALUES ('K1', 'backup.folder', 'content://x')",
)

private fun JsonObject.str(key: String) = getValue(key).jsonPrimitive.content

/** Creates [file] as the schema-1 app left it; returns each table's schema-1 columns in order. */
private fun createSchema1(file: File): Map<String, List<String>> {
    val db = Json.parseToJsonElement(schema1Json.readText()).jsonObject.getValue("database").jsonObject
    val columns = linkedMapOf<String, List<String>>()
    raw(file) { c ->
        for (e in db.getValue("entities").jsonArray.map { it.jsonObject }) {
            val table = e.str("tableName")
            c.execSQL(e.str("createSql").replace("\${TABLE_NAME}", table))
            e["indices"]?.jsonArray?.forEach { c.execSQL(it.jsonObject.str("createSql").replace("\${TABLE_NAME}", table)) }
            columns[table] = e.getValue("fields").jsonArray.map { it.jsonObject.str("columnName") }
        }
        db.getValue("setupQueries").jsonArray.forEach { c.execSQL(it.jsonPrimitive.content) }
        c.execSQL("PRAGMA user_version = 1")
        c.execSQL("PRAGMA foreign_keys = ON")
        schema1Rows.forEach(c::execSQL)
    }
    return columns
}

private fun <T> raw(file: File, block: (SQLiteConnection) -> T): T {
    val c = BundledSQLiteDriver().open(file.path)
    try {
        return block(c)
    } finally {
        c.close()
    }
}

private fun SQLiteConnection.rows(sql: String): List<List<String>> = prepare(sql).use { s ->
    buildList { while (s.step()) add(List(s.getColumnCount()) { if (s.isNull(it)) "NULL" else s.getText(it) }) }
}

/** Every cell as SQLite's own literal (quote()): type and bytes both, so 1 and '1' differ. */
private fun SQLiteConnection.cells(table: String, cols: List<String>) =
    rows("SELECT ${cols.joinToString { "quote(`$it`)" }} FROM `$table` ORDER BY id")

/** A table's shape: columns in order with type, not-null, default and key; indices; foreign keys. */
private fun SQLiteConnection.shape(table: String): List<List<String>> =
    rows("PRAGMA table_info(`$table`)") +
        rows("PRAGMA index_list(`$table`)").sortedBy { it[1] }.flatMap { ix ->
            listOf(listOf(ix[1], ix[2])) + rows("PRAGMA index_info(`${ix[1]}`)")
        } +
        rows("PRAGMA foreign_key_list(`$table`)").map { it.drop(2) }

private val dirs = mutableListOf<File>()

private fun tempFile(): File = File(Files.createTempDirectory("sisyphus").toFile().also { dirs += it }, DATABASE_NAME)

class MigrationTest {
    @AfterTest fun clean() = dirs.forEach { it.deleteRecursively() }

    @Test
    fun everySchema1RowSurvivesByteForByteAndTheNewPartsStartAtTheirDefaults() {
        val file = tempFile()
        val v1Columns = createSchema1(file)
        val before = raw(file) { c -> v1Columns.mapValues { (t, cols) -> c.cells(t, cols) } }
        assertEquals(schema1Rows.size, before.values.sumOf { it.size })

        val open = openSisyphusDatabase(file)
        open.database.close()
        assertFalse(open.recovered)
        assertEquals(listOf(DATABASE_NAME), file.parentFile!!.list()!!.filter { UNOPENABLE_SUFFIX in it || it == DATABASE_NAME })

        raw(file) { c ->
            assertEquals(before, v1Columns.mapValues { (t, cols) -> c.cells(t, cols) })
            assertEquals(listOf(listOf("2")), c.rows("PRAGMA user_version"))
            assertEquals(emptyList(), c.rows("PRAGMA foreign_key_check"))
            assertEquals(List(2) { listOf("0") }, c.cells("exercise", listOf("show_partial_reps")))
            assertEquals(List(3) { listOf("0", "0") }, c.cells("logged_set", listOf("power", "partial_reps")))
            for (table in TABLES - v1Columns.keys) assertEquals(listOf(listOf("0")), c.rows("SELECT count(*) FROM `$table`"), table)
            assertEquals(setOf("exercise_module", "label", "session_label", "body_measure"), (TABLES - v1Columns.keys).toSet())
        }
    }

    @Test
    fun aMigratedDatabaseHasExactlyTheShapeOfAFreshOne() {
        val migrated = tempFile().also { createSchema1(it) }
        val fresh = tempFile()
        listOf(migrated, fresh).forEach { openSisyphusDatabase(it).database.close() }
        val a = raw(migrated) { c -> TABLES.associateWith { c.shape(it) } }
        val b = raw(fresh) { c -> TABLES.associateWith { c.shape(it) } }
        TABLES.forEach { assertEquals(b[it], a[it], it) }
    }

    @Test
    fun aMigratedDatabaseTakesTheNewRowsAndPassesTheBackupGate() = runTest {
        val file = tempFile().also { createSchema1(it) }
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
}
