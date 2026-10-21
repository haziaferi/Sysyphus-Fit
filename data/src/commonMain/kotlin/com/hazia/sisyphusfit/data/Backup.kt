package com.hazia.sisyphusfit.data

import androidx.room.PooledConnection
import androidx.room.useReaderConnection
import androidx.room.useWriterConnection
import androidx.room.immediateTransaction
import androidx.sqlite.SQLiteStatement
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull

/*
 * The one-file backup (harvest row KA9, SPEC §5): every table in [TABLES], every column, rows by id.
 * It is generic on purpose: it reads the columns from the database itself (PRAGMA table_info), so a
 * column added by a later schema is in the next backup without anyone remembering to add it.
 */

const val BACKUP_FORMAT = "sisyphusfit-backup"
/** 2 since schema 2 (four more tables, three more columns); a version-1 file still restores. */
const val BACKUP_VERSION = 2

class BackupException(message: String) : Exception(message)

private val json = Json { prettyPrint = true }

/** Every row of every table as JSON text. Two exports of the same data are byte-identical. */
suspend fun exportBackup(db: SisyphusDatabase): String = db.useReaderConnection { c ->
    val tables = linkedMapOf<String, JsonElement>()
    for (table in TABLES) {
        val cols = columns(c, table)
        val rows = c.usePrepared("SELECT ${cols.joinToString { "`${it.name}`" }} FROM `$table` ORDER BY id") { s ->
            buildList { while (s.step()) add(JsonObject(cols.mapIndexed { i, col -> col.name to read(s, i, col) }.toMap())) }
        }
        tables[table] = JsonArray(rows)
    }
    json.encodeToString(
        JsonElement.serializer(),
        JsonObject(
            linkedMapOf(
                "format" to JsonPrimitive(BACKUP_FORMAT),
                "version" to JsonPrimitive(BACKUP_VERSION),
                "schema" to JsonPrimitive(SCHEMA_VERSION),
                "tables" to JsonObject(tables),
            ),
        ),
    )
}

/**
 * Replaces everything with [text] in one transaction: on any refusal or failure nothing changes.
 * Refuses another format, a newer backup or schema, an unknown table and a column this database
 * does not have; a table or column the backup lacks is left empty or null.
 */
suspend fun restoreBackup(db: SisyphusDatabase, text: String) {
    val root = runCatching { json.parseToJsonElement(text).jsonObject }.getOrElse { throw BackupException("not a backup file") }
    if (root["format"]?.jsonPrimitive?.content != BACKUP_FORMAT) throw BackupException("not a SisyphusFit backup")
    val version = root["version"]?.jsonPrimitive?.int ?: throw BackupException("no version")
    if (version > BACKUP_VERSION) throw BackupException("backup version $version is newer than this app")
    val schema = root["schema"]?.jsonPrimitive?.int ?: throw BackupException("no schema")
    if (schema > SCHEMA_VERSION) throw BackupException("backup schema $schema is newer than this app")
    val tables = root["tables"]?.jsonObject ?: throw BackupException("no tables")
    tables.keys.firstOrNull { it !in TABLES }?.let { throw BackupException("unknown table $it") }

    db.useWriterConnection { t ->
        t.immediateTransaction {
            for (table in TABLES.reversed()) exec(this, "DELETE FROM `$table`")
            for (table in TABLES) {
                val cols = columns(this, table).associateBy { it.name }
                for (row in tables[table]?.jsonArray.orEmpty()) {
                    val obj = row.jsonObject
                    obj.keys.firstOrNull { it !in cols }?.let { throw BackupException("$table has no column $it") }
                    val names = obj.keys.toList()
                    val sql = "INSERT INTO `$table` (${names.joinToString { "`$it`" }}) VALUES (${names.joinToString { "?" }})"
                    usePrepared(sql) { s ->
                        names.forEachIndexed { i, n -> bind(s, i + 1, cols.getValue(n), obj.getValue(n)) }
                        s.step()
                    }
                }
            }
        }
    }
}

private class Column(val name: String, val type: String)

private suspend fun columns(c: PooledConnection, table: String): List<Column> =
    c.usePrepared("PRAGMA table_info(`$table`)") { s ->
        buildList { while (s.step()) add(Column(s.getText(1), s.getText(2).uppercase())) }
    }

private suspend fun exec(c: PooledConnection, sql: String) = c.usePrepared(sql) { it.step() }

private fun read(s: SQLiteStatement, i: Int, col: Column): JsonElement = when {
    s.isNull(i) -> JsonNull
    col.type.contains("INT") -> JsonPrimitive(s.getLong(i))
    col.type.contains("REAL") -> JsonPrimitive(s.getDouble(i))
    else -> JsonPrimitive(s.getText(i))
}

private fun bind(s: SQLiteStatement, index: Int, col: Column, v: JsonElement) {
    if (v is JsonNull) return s.bindNull(index)
    val p = v.jsonPrimitive
    when {
        col.type.contains("INT") -> s.bindLong(index, p.longOrNull ?: throw BackupException("${col.name}: not an integer"))
        col.type.contains("REAL") -> s.bindDouble(index, p.doubleOrNull ?: throw BackupException("${col.name}: not a number"))
        else -> s.bindText(index, p.content)
    }
}
