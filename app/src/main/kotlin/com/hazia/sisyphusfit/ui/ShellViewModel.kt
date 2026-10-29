package com.hazia.sisyphusfit.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hazia.sisyphusfit.core.Module
import com.hazia.sisyphusfit.data.Settings
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * What the shell shows: nothing while the setting loads, the module choice until one is recorded (0.18), then the tabs;
 * [Failed] when the data cannot be opened (owner's call, 0.60), on any launch.
 */
sealed interface ShellState {
    data object Loading : ShellState
    data object Choose : ShellState
    data object Ready : ShellState
    data object Failed : ShellState
}

class ShellViewModel(private val settings: Settings) : ViewModel() {
    private val attempts = MutableStateFlow(0)

    // Each attempt reads afresh: a failed open is not kept by the container, so trying again opens again.
    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<ShellState> = attempts
        .flatMapLatest {
            settings.modules()
                .map<Set<Module>?, ShellState> { if (it == null) ShellState.Choose else ShellState.Ready }
                .catch { emit(ShellState.Failed) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ShellState.Loading)

    // NonCancellable: Back right after "Step forward" ends this ViewModel; the choice is recorded all the same.
    fun choose(modules: Set<Module>) {
        viewModelScope.launch { withContext(NonCancellable) { settings.chooseModules(modules) } }
    }

    fun retry() {
        attempts.value++
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { ShellViewModel(container.settings) }
        }
    }
}
