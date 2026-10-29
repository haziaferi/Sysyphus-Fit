package com.hazia.sisyphusfit

import android.os.Looper
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.hazia.sisyphusfit.core.Module
import com.hazia.sisyphusfit.data.DatabaseOpen
import com.hazia.sisyphusfit.data.Settings
import com.hazia.sisyphusfit.data.SisyphusDatabase
import com.hazia.sisyphusfit.data.WallClock
import kotlinx.coroutines.runBlocking
import org.junit.rules.ExternalResource

/**
 * Gives the app a container on an in-memory database before any activity starts (an activity rule launches
 * before @Before), and counts the opens and where they ran. [chosen] is the module choice already recorded, as after
 * a first launch; null leaves it unrecorded, so the app opens on the choice (0.18). While [broken], every open fails.
 */
class TestContainerRule(private val chosen: Set<Module>? = emptySet(), @Volatile var broken: Boolean = false) : ExternalResource() {
    lateinit var db: SisyphusDatabase
    @Volatile var opens = 0
    @Volatile var openedOnMain: Boolean? = null

    override fun before() {
        val app = ApplicationProvider.getApplicationContext<SisyphusApp>()
        db = Room.inMemoryDatabaseBuilder<SisyphusDatabase>(app).build()
        // Straight into the database, not through the container, so the counts stay the app's own.
        chosen?.let { runBlocking { Settings({ db }, WallClock.System).chooseModules(it) } }
        app.container = AppContainer({
            opens++
            openedOnMain = Looper.myLooper() == Looper.getMainLooper()
            check(!broken) { "the database could not be opened" }
            DatabaseOpen(db, recovered = false)
        })
    }

    override fun after() = db.close()
}
