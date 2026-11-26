package com.hazia.sisyphusfit.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hazia.sisyphusfit.core.Module
import com.hazia.sisyphusfit.data.Settings
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * What the shell shows: nothing while the setting loads, the module choice until one is recorded (SPEC:R90), then the tabs;
 * [Failed] when the data cannot be opened (owner's call, SPEC:R92), on any launch; [SetAside] first, on every launch until
 * dismissed, when a damaged file was set aside (SPEC:R122).
 */
sealed interface ShellState {
    data object Loading : ShellState
    data object SetAside : ShellState
    data object Choose : ShellState
    data object Ready : ShellState
    data object Failed : ShellState
}

/**
 * [setAsideStamp] reads the stamp of the newest file set aside on disk (SPEC:R122): the file, not the database, marks the
 * notice as owed, so a failed open or a killed process after the set-aside cannot lose it.
 */
class ShellViewModel(private val settings: Settings, private val setAsideStamp: suspend () -> Long? = { null }) : ViewModel() {
    private val attempts = MutableStateFlow(0)
    @Volatile private var owed: Long? = null

    // Each attempt reads afresh: a failed open is not kept by the container, so trying again opens again.
    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<ShellState> = attempts
        .flatMapLatest {
            flow {
                val stamp = setAsideStamp()
                emitAll(
                    combine(settings.setAsideSeen(), settings.modules()) { seen, modules ->
                        owed = stamp?.takeIf { seen == null || seen < it }
                        when {
                            owed != null -> ShellState.SetAside
                            modules == null -> ShellState.Choose
                            else -> ShellState.Ready
                        }
                    },
                )
            }.catch { emit(ShellState.Failed) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ShellState.Loading)

    // A failed write keeps the screen up, so "Continue" can be tapped again, rather than ending the app.
    fun dismissSetAside() {
        val stamp = owed ?: return
        viewModelScope.launch {
            withContext(NonCancellable) {
                try {
                    settings.seeSetAside(stamp)
                } catch (e: CancellationException) {
                    throw e
                } catch (_: Exception) {
                    // Nothing was written, so the notice stays owed and shows again.
                }
            }
        }
    }

    // NonCancellable: Back right after "Step forward" ends this ViewModel; the choice is recorded all the same.
    fun choose(modules: Set<Module>) {
        viewModelScope.launch { withContext(NonCancellable) { settings.chooseModules(modules) } }
    }

    fun retry() {
        attempts.value++
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { ShellViewModel(container.settings, container::setAsideStamp) }
        }
    }
}
