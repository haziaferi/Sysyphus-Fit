package com.hazia.sisyphusfit.data

import androidx.room.useReaderConnection
import java.io.File
import java.nio.file.Files

/** A database in its own temporary folder, opened the app's way; [reopen] opens the same file again. */
class TempDatabase {
    val file: File = File(Files.createTempDirectory("sisyphus").toFile(), DATABASE_NAME)
    val db: SisyphusDatabase = reopen()

    fun reopen(): SisyphusDatabase = openSisyphusDatabase(file).database

    fun close() {
        db.close()
        file.parentFile.deleteRecursively()
    }
}

/** A clock the test moves: [now] and [zone] are read on every call. */
class TestClock(var now: Long = 1_000, var zone: String = "Europe/Rome") {
    val clock = WallClock({ now }, { zone })
}

suspend fun SisyphusDatabase.count(sql: String): Long =
    useReaderConnection { c -> c.usePrepared(sql) { s -> s.step(); s.getLong(0) } }
