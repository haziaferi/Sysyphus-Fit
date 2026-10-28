package com.hazia.sisyphusfit

import android.content.Context
import android.os.Looper
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hazia.sisyphusfit.data.DATABASE_NAME
import com.hazia.sisyphusfit.data.DatabaseOpen
import com.hazia.sisyphusfit.data.SisyphusDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/** The one container, wired by hand (SPEC 0.58): the database opens once, on first use, never on the main thread. */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class AppContainerTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val db = Room.inMemoryDatabaseBuilder<SisyphusDatabase>(context).build()

    @After fun close() = db.close()

    @Test fun nothingOpensUntilFirstUse() {
        var opens = 0
        AppContainer({ opens++; DatabaseOpen(db, recovered = false) })
        assertEquals(0, opens)
    }

    @Test fun opensOnceForEveryStore() = runBlocking {
        var opens = 0
        val c = AppContainer({ opens++; DatabaseOpen(db, recovered = false) })
        c.settings.modules().first()
        c.exercises.active().first()
        c.sessions.open().first()
        assertEquals(1, opens)
    }

    // Robolectric runs the test on the main looper's thread: asking from here is asking as a ViewModel on Main would.
    @Test fun neverOpensOnTheMainThread() {
        assertTrue(Looper.myLooper() == Looper.getMainLooper())
        var onMain: Boolean? = null
        val c = AppContainer({ onMain = Looper.myLooper() == Looper.getMainLooper(); DatabaseOpen(db, recovered = false) })
        runBlocking { c.settings.modules().first() }
        assertEquals(false, onMain)
    }

    @Test fun appStartsWithTheRealFileUntouched() {
        val app = ApplicationProvider.getApplicationContext<SisyphusApp>()
        app.container // built with the app
        assertFalse(app.getDatabasePath(DATABASE_NAME).exists())
    }
}
