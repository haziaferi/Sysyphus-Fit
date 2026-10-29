package com.hazia.sisyphusfit.ui

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hazia.sisyphusfit.AppContainer
import com.hazia.sisyphusfit.SisyphusApp
import com.hazia.sisyphusfit.core.Module
import com.hazia.sisyphusfit.data.DatabaseOpen
import com.hazia.sisyphusfit.data.SisyphusDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withTimeout
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/** The shell's gate: the choice only while nothing is recorded (0.18); never a flash of it once something is. */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class ShellViewModelTest {
    private val app = ApplicationProvider.getApplicationContext<SisyphusApp>()
    private val db = Room.inMemoryDatabaseBuilder<SisyphusDatabase>(app).build()
    private val container = AppContainer({ DatabaseOpen(db, recovered = false) })

    @Before fun main() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @After fun close() {
        Dispatchers.resetMain()
        db.close()
    }

    private fun <T> waitFor(block: suspend () -> T): T = runBlocking { withTimeout(5_000) { block() } }

    @Test fun nothingRecordedAsksForTheChoice() {
        val vm = ShellViewModel(container.settings)
        assertEquals(ShellState.Loading, vm.state.value)
        assertEquals(ShellState.Choose, waitFor { vm.state.first { it != ShellState.Loading } })
    }

    @Test fun aChosenAppNeverPassesThroughTheChoice() {
        waitFor { container.settings.chooseModules(setOf(Module.STRENGTH)) }
        val vm = ShellViewModel(container.settings)
        val seen = waitFor { vm.state.takeWhile { it != ShellState.Ready }.toList() }
        assertEquals(listOf(ShellState.Loading), seen)
    }

    @Test fun choosingRecordsAndOpensTheTabs() {
        val vm = ShellViewModel(container.settings)
        waitFor { vm.state.first { it == ShellState.Choose } }
        vm.choose(setOf(Module.STAMINA))
        waitFor { vm.state.first { it == ShellState.Ready } }
        assertEquals(setOf(Module.STAMINA), waitFor { container.settings.modules().first() })
    }

    // Planned ruling: a read that fails opens the tabs, where Start's own failed state (0.58) shows it.
    @Test fun aFailedReadOpensTheTabs() {
        val broken = AppContainer({ throw IllegalStateException("could not set aside") })
        val vm = ShellViewModel(broken.settings)
        assertEquals(ShellState.Ready, waitFor { vm.state.first { it != ShellState.Loading } })
    }
}
