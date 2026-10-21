package com.hazia.sisyphusfit.data

import androidx.sqlite.SQLiteConnection
import androidx.sqlite.SQLiteDriver
import androidx.sqlite.execSQL
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.File
import kotlin.test.assertEquals

/*
 * A database as the schema-1 app left it, for the migration tests on both platforms. It is built from
 * Room's own exported 1.json, not from today's entities.
 */

private val schema1Json = File("schemas/com.hazia.sisyphusfit.data.SisyphusDatabase/1.json")

/** Every schema-1 table filled, with nulls, non-ASCII text, a newline and extreme integers. */
internal val schema1Rows = listOf(
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

/**
 * Creates [file] as the schema-1 app left it, then runs [extra] (a test's own damage, if any);
 * returns each table's schema-1 columns in order.
 */
internal fun createSchema1(file: File, driver: SQLiteDriver, extra: List<String> = emptyList()): Map<String, List<String>> {
    val db = Json.parseToJsonElement(schema1Json.readText()).jsonObject.getValue("database").jsonObject
    val columns = linkedMapOf<String, List<String>>()
    file.parentFile?.mkdirs()
    raw(file, driver) { c ->
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
        extra.forEach(c::execSQL)
    }
    return columns
}

internal fun <T> raw(file: File, driver: SQLiteDriver, block: (SQLiteConnection) -> T): T {
    val c = driver.open(file.path)
    try {
        return block(c)
    } finally {
        c.close()
    }
}

internal fun SQLiteConnection.rows(sql: String): List<List<String>> = prepare(sql).use { s ->
    buildList { while (s.step()) add(List(s.getColumnCount()) { if (s.isNull(it)) "NULL" else s.getText(it) }) }
}

/** Every cell as SQLite's own literal (quote()): type and bytes both, so 1 and '1' differ. */
internal fun SQLiteConnection.cells(table: String, cols: List<String>) =
    rows("SELECT ${cols.joinToString { "quote(`$it`)" }} FROM `$table` ORDER BY id")

/** Every schema-1 cell of every schema-1 table. */
internal fun SQLiteConnection.allCells(v1Columns: Map<String, List<String>>) = v1Columns.mapValues { (t, cols) -> cells(t, cols) }

/** A table's shape: columns in order with type, not-null, default and key; indices; foreign keys. */
internal fun SQLiteConnection.shape(table: String): List<List<String>> =
    rows("PRAGMA table_info(`$table`)") +
        rows("PRAGMA index_list(`$table`)").sortedBy { it[1] }.flatMap { ix ->
            listOf(listOf(ix[1], ix[2])) + rows("PRAGMA index_info(`${ix[1]}`)")
        } +
        rows("PRAGMA foreign_key_list(`$table`)").map { it.drop(2) }

/** The shape of every table in the file, by name. */
internal fun SQLiteConnection.everyShape(): Map<String, List<List<String>>> =
    rows("SELECT name FROM sqlite_master WHERE type = 'table' AND name NOT LIKE 'sqlite_%' ORDER BY name")
        .associate { (name) -> name to shape(name) }

/** What a migrated file must hold beyond its schema-1 cells: version 2, defaults 0, new tables empty. */
internal fun assertMigrated(c: SQLiteConnection, v1Columns: Map<String, List<String>>) {
    assertEquals(listOf(listOf("2")), c.rows("PRAGMA user_version"))
    assertEquals(emptyList(), c.rows("PRAGMA foreign_key_check"))
    assertEquals(List(2) { listOf("0") }, c.cells("exercise", listOf("show_partial_reps")))
    assertEquals(List(3) { listOf("0", "0") }, c.cells("logged_set", listOf("power", "partial_reps")))
    val added = TABLES - v1Columns.keys
    assertEquals(setOf("exercise_module", "label", "session_label", "body_measure"), added.toSet())
    for (table in added) assertEquals(listOf(listOf("0")), c.rows("SELECT count(*) FROM `$table`"), table)
}

/**
 * Damage that makes the open's migration step fail, one per way it can fail: a statement that
 * throws (the column already exists); a migration that runs but leaves a shape Room refuses (a table
 * of the same name, wrong columns, skipped by CREATE TABLE IF NOT EXISTS); and a file a newer app
 * wrote (a downgrade, which has no migration: the case a destructive fallback would wipe).
 */
internal val migrationBreakers = mapOf(
    "a statement throws" to listOf("ALTER TABLE logged_set ADD COLUMN power INTEGER"),
    "the shape is wrong" to listOf("CREATE TABLE label (id TEXT NOT NULL PRIMARY KEY, title TEXT)"),
    "a newer app wrote it" to listOf("PRAGMA user_version = 3"),
)

/** Everything a failed open must leave alone: every schema-1 cell, every table's shape, the version. */
internal fun SQLiteConnection.untouchable(v1Columns: Map<String, List<String>>) =
    Triple(allCells(v1Columns), everyShape(), rows("PRAGMA user_version"))
