package com.hazia.sisyphusfit.data

import com.hazia.sisyphusfit.core.Module
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

/** The modules chosen at first launch (0.18): nothing is assumed until the person chooses. */
class SettingsTest {
    private val temp = TempDatabase()
    private val db = temp.db
    private val settings = Settings(db = { db }, clock = TestClock().clock)

    @AfterTest fun close() = temp.close()

    @Test fun modulesAreNullUntilChosen() = runBlocking {
        assertNull(settings.modules().first())
    }

    @Test fun choosingNoneIsAChoice() = runBlocking {
        settings.chooseModules(emptySet())
        assertEquals(emptySet<Module>(), settings.modules().first())
    }

    @Test fun choiceRoundTripsAndReplaces() = runBlocking {
        settings.chooseModules(setOf(Module.STRENGTH, Module.DAILY_MOVEMENT))
        settings.chooseModules(setOf(Module.STAMINA))
        assertEquals(setOf(Module.STAMINA), settings.modules().first())
        assertEquals(1L, db.count("SELECT count(*) FROM setting WHERE `key` = 'modules'"))
    }

    // A backup from a newer build may name a module this one does not know.
    @Test fun unknownModuleNameIsSkipped() = runBlocking {
        db.settingDao().put("X", "modules", "STRENGTH,JUGGLING")
        assertEquals(setOf(Module.STRENGTH), settings.modules().first())
    }

    // A screen watching the choice sees a restore, not the rows it replaced.
    @Test fun modulesFlowSeesARestore() = runBlocking {
        settings.chooseModules(setOf(Module.STRENGTH))
        val backup = exportBackup(db)
        settings.chooseModules(setOf(Module.STAMINA))
        val seen = Channel<Set<Module>?>(Channel.UNLIMITED)
        val watch = launch(Dispatchers.Default) { settings.modules().collect { seen.send(it) } }
        assertEquals(setOf(Module.STAMINA), withTimeout(5_000) { seen.receive() })
        restoreBackup(db, backup)
        assertEquals(setOf(Module.STRENGTH), withTimeout(5_000) { seen.receive() })
        watch.cancel()
    }
}
