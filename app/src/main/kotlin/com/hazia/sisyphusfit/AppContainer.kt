package com.hazia.sisyphusfit

import com.hazia.sisyphusfit.data.DatabaseOpen
import com.hazia.sisyphusfit.data.Db
import com.hazia.sisyphusfit.data.ExerciseStore
import com.hazia.sisyphusfit.data.SessionStore
import com.hazia.sisyphusfit.data.Settings
import com.hazia.sisyphusfit.data.SisyphusDatabase
import com.hazia.sisyphusfit.data.WallClock
import com.hazia.sisyphusfit.data.oweSetAsideNotice
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

/**
 * Everything the screens need, wired by hand (SPEC:R84). The database opens once, on first use, on [io]:
 * `openDatabase` blocks while it probes, and a corrupt file is set aside there, never on the main thread.
 */
class AppContainer(
    private val open: () -> DatabaseOpen<SisyphusDatabase>,
    private val clock: WallClock = WallClock.System,
    private val io: CoroutineDispatcher = Dispatchers.IO,
) {
    private val lock = Mutex()
    @Volatile private var opened: DatabaseOpen<SisyphusDatabase>? = null

    // NonCancellable: a screen left during a slow first open must not throw its result away (a leaked database, and a
    // second open that no longer knows a corrupt file was set aside). A file set aside is noted in the fresh database,
    // so the person is told until they dismiss it (SPEC:R35, SPEC:R122).
    private val db: Db = {
        opened?.database ?: lock.withLock {
            opened?.database ?: withContext(io + NonCancellable) {
                open().also { if (it.recovered) it.database.oweSetAsideNotice(clock); opened = it }
            }.database
        }
    }

    val settings = Settings(db, clock)
    val exercises = ExerciseStore(db, clock)
    val sessions = SessionStore(db, clock)
}
