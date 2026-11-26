package com.hazia.sisyphusfit

import com.hazia.sisyphusfit.data.DatabaseOpen
import com.hazia.sisyphusfit.data.Db
import com.hazia.sisyphusfit.data.ExerciseStore
import com.hazia.sisyphusfit.data.SessionStore
import com.hazia.sisyphusfit.data.Settings
import com.hazia.sisyphusfit.data.SisyphusDatabase
import com.hazia.sisyphusfit.data.WallClock
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/**
 * Everything the screens need, wired by hand (SPEC:R84). The database opens once, on first use, on [io]:
 * `openDatabase` blocks while it probes, and a corrupt file is set aside there, never on the main thread.
 * [setAsideStampOnDisk] gives the stamp of the newest file set aside beside the database, or null (SPEC:R122).
 */
class AppContainer(
    private val open: () -> DatabaseOpen<SisyphusDatabase>,
    clock: WallClock = WallClock.System,
    private val io: CoroutineDispatcher = Dispatchers.IO,
    private val setAsideStampOnDisk: () -> Long? = { null },
) {
    private val lock = Mutex()
    @Volatile private var opened: DatabaseOpen<SisyphusDatabase>? = null

    // NonCancellable: a screen left during a slow first open must not throw its result away (a leaked database).
    private val db: Db = {
        opened?.database ?: lock.withLock {
            opened?.database ?: withContext(io + NonCancellable) { open().also { opened = it } }.database
        }
    }

    /** Reads the directory off the main thread. */
    suspend fun setAsideStamp(): Long? = withContext(io) { setAsideStampOnDisk() }

    val settings = Settings(db, clock)
    val exercises = ExerciseStore(db, clock)
    val sessions = SessionStore(db, clock)
}
