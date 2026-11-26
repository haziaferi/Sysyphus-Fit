package com.hazia.sisyphusfit.ui.start

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewmodel.MutableCreationExtras
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hazia.sisyphusfit.AppContainer
import com.hazia.sisyphusfit.SisyphusApp
import com.hazia.sisyphusfit.core.Module
import com.hazia.sisyphusfit.data.DatabaseOpen
import com.hazia.sisyphusfit.data.ExerciseKind
import com.hazia.sisyphusfit.data.SetValues
import com.hazia.sisyphusfit.data.SisyphusDatabase
import com.hazia.sisyphusfit.data.WallClock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
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

/** The pattern every screen copies (SPEC:R84): a ViewModel per screen, its stores from the app's container. */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class StartViewModelTest {
    private val app = ApplicationProvider.getApplicationContext<SisyphusApp>()
    private val db = Room.inMemoryDatabaseBuilder<SisyphusDatabase>(app).build()
    private val container = AppContainer({ DatabaseOpen(db, recovered = false) })

    @Before fun main() = Dispatchers.setMain(UnconfinedTestDispatcher())

    @After fun close() {
        Dispatchers.resetMain()
        db.close()
    }

    private fun viewModel() = StartViewModel(container.sessions, container.settings)

    private fun <T> waitFor(block: suspend () -> T): T = runBlocking { withTimeout(5_000) { block() } }

    @Test fun startsLoadingThenShowsNoResume() {
        val vm = viewModel()
        assertEquals(StartState(), vm.state.value)
        assertEquals(StartState(loading = false), waitFor { vm.state.first { !it.loading } })
    }

    @Test fun anOpenSessionIsOfferedForResume() {
        val vm = viewModel()
        waitFor {
            val ex = container.exercises.add("Squat", ExerciseKind.DYNAMIC, false, setOf(Module.STRENGTH))
            val s = container.sessions.start()
            container.sessions.logSet(s, ex, SetValues(reps = 5))
        }
        assertEquals(1, waitFor { vm.state.first { it.resume?.sets?.size == 1 } }.resume!!.sets.size)
    }

    // SPEC:R96: opening Start the next morning closes yesterday's idle session, so the module cards show again.
    @Test fun openingStartClosesAnIdleSessionFromAnEarlierDay() {
        var now = 1_761_678_000_000L // Tuesday 2025-10-28 19:00 UTC
        val timed = AppContainer({ DatabaseOpen(db, recovered = false) }, WallClock({ now }, { "UTC" }))
        waitFor {
            val ex = timed.exercises.add("Squat", ExerciseKind.DYNAMIC, false, setOf(Module.STRENGTH))
            timed.sessions.logSet(timed.sessions.start(), ex, SetValues(reps = 5))
        }
        now += 14 * 3_600_000L
        val vm = StartViewModel(timed.sessions, timed.settings)
        val shown = waitFor { vm.state.first { !it.loading } }
        assertEquals(false, shown.failed)
        assertEquals(null, shown.resume)
        assertEquals(null, waitFor { timed.sessions.open().first() })
    }

    @Test fun chosenModulesFlowThrough() {
        val vm = viewModel()
        waitFor { container.settings.chooseModules(setOf(Module.STRENGTH)) }
        assertEquals(setOf(Module.STRENGTH), waitFor { vm.state.first { it.modules.isNotEmpty() } }.modules)
    }

    // Review I3: a database that will not open (a full disk, a file that cannot be set aside) gives a state the
    // screen can show, not a crash at every launch.
    @Test fun aFailedOpenIsAStateNotACrash() {
        val broken = AppContainer({ throw IllegalStateException("could not set aside") })
        val vm = StartViewModel(broken.sessions, broken.settings)
        assertEquals(StartState(loading = false, failed = true), waitFor { vm.state.first { !it.loading } })
    }

    @Test fun factoryTakesTheStoresFromTheApp() {
        app.container = container
        waitFor { container.sessions.start() }
        val extras = MutableCreationExtras().apply { set(APPLICATION_KEY, app) }
        val vm = ViewModelProvider.create(ViewModelStore(), StartViewModel.Factory, extras)[StartViewModel::class]
        waitFor { vm.state.first { it.resume != null } }
    }
}
