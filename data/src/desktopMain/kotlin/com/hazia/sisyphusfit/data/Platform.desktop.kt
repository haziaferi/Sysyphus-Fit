package com.hazia.sisyphusfit.data

import androidx.room.Room
import androidx.sqlite.SQLiteException
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import java.io.File

// The desktop JVM target: where the tests run, and the start of a Windows build (SPEC 0.1).
// BundledSQLiteDriver never deletes a corrupt file, so it needs no wrapper.

fun openSisyphusDatabase(file: File, now: () -> Long = System::currentTimeMillis): DatabaseOpen<SisyphusDatabase> =
    openDatabase(
        file = file,
        builder = { Room.databaseBuilder<SisyphusDatabase>(name = file.absolutePath) },
        driver = BundledSQLiteDriver(),
        now = now,
    )

// androidx.sqlite exposes no result code; its own throw site formats "Error code: N, message: ...".
// Extended codes keep the primary code in the low byte.
private val errorCode = Regex("""^Error code: (\d+)""")
private const val SQLITE_CORRUPT = 11
private const val SQLITE_NOTADB = 26

actual fun isCorruptDatabase(failure: Throwable): Boolean =
    generateSequence(failure) { it.cause }.any { t ->
        val code = (t as? SQLiteException)?.message?.let { errorCode.find(it) }?.groupValues?.get(1)?.toInt()
        code != null && (code and 0xFF) in setOf(SQLITE_CORRUPT, SQLITE_NOTADB)
    }
