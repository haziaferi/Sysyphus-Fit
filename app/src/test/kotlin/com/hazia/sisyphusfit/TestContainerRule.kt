package com.hazia.sisyphusfit

import android.os.Looper
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.hazia.sisyphusfit.data.DatabaseOpen
import com.hazia.sisyphusfit.data.SisyphusDatabase
import org.junit.rules.ExternalResource

/**
 * Gives the app a container on an in-memory database before any activity starts (an activity rule launches
 * before @Before), and counts the opens and where they ran.
 */
class TestContainerRule : ExternalResource() {
    lateinit var db: SisyphusDatabase
    @Volatile var opens = 0
    @Volatile var openedOnMain: Boolean? = null

    override fun before() {
        val app = ApplicationProvider.getApplicationContext<SisyphusApp>()
        db = Room.inMemoryDatabaseBuilder<SisyphusDatabase>(app).build()
        app.container = AppContainer({
            opens++
            openedOnMain = Looper.myLooper() == Looper.getMainLooper()
            DatabaseOpen(db, recovered = false)
        })
    }

    override fun after() = db.close()
}
