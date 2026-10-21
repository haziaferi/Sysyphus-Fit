package com.hazia.sisyphusfit.data

import androidx.room.RoomDatabase
import androidx.room.useReaderConnection
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.SQLiteDriver
import androidx.sqlite.execSQL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.io.File

// Adapted from Factotum (data/jvmCommon/OpenDatabase.kt and SetAside.kt), the owner's own code.

/**
 * The one way the app opens its database on either platform: through [openOrRecover], so a corrupt
 * file is set aside rather than lost. Blocks while it probes; call it off the main thread.
 */
internal fun openDatabase(
    file: File,
    builder: () -> RoomDatabase.Builder<SisyphusDatabase>,
    driver: SQLiteDriver,
    now: () -> Long = System::currentTimeMillis,
): DatabaseOpen<SisyphusDatabase> {
    file.parentFile?.mkdirs()
    val tracked = TrackingDriver(driver)
    return openOrRecover(
        build = { builder().setDriver(tracked).addMigrations(*MIGRATIONS).setQueryCoroutineContext(Dispatchers.IO).build() },
        // Room opens lazily; a real read forces the open and schema creation.
        probe = { db ->
            runBlocking { db.useReaderConnection { it.usePrepared("SELECT count(*) FROM sqlite_master") { s -> s.step() } } }
        },
        close = {
            it.close()
            tracked.closeAll()
        },
        setAside = { setAsideDatabaseFiles(file.path, now()) },
        isUnopenable = ::isCorruptDatabase,
    )
}

/**
 * Remembers every connection it opens. Room keeps its connection open when its own open fails, and
 * Windows cannot rename an open file, so recovery closes them all before setting the file aside.
 * Foreign keys are switched on for every connection: SQLite leaves them off by default.
 */
private class TrackingDriver(private val driver: SQLiteDriver) : SQLiteDriver by driver {
    private val opened = mutableListOf<SQLiteConnection>()

    override fun open(fileName: String): SQLiteConnection =
        driver.open(fileName).also { synchronized(opened) { opened += it } }.also { it.execSQL("PRAGMA foreign_keys = ON") }

    fun closeAll() = synchronized(opened) {
        opened.forEach { runCatching { it.close() } }
        opened.clear()
    }
}

/**
 * Renames [dbPath] and its `-journal`, `-wal` and `-shm` files to `<name>.unopenable-<stamp>`, so the
 * new database is never opened beside the old one's journal. The database file moves last: if a
 * rename fails, it is still in place and the next start tries again.
 */
fun setAsideDatabaseFiles(dbPath: String, stamp: Long) {
    for (suffix in listOf("-journal", "-wal", "-shm", "")) {
        val file = File(dbPath + suffix)
        if (file.exists()) {
            check(file.renameTo(File(file.path + UNOPENABLE_SUFFIX + stamp))) { "could not set aside ${file.path}" }
        }
    }
}
