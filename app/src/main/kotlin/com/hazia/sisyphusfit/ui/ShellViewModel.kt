package com.hazia.sisyphusfit.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hazia.sisyphusfit.core.Module
import com.hazia.sisyphusfit.data.Settings
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** What the shell shows: nothing while the setting loads, the module choice until one is recorded (0.18), then the tabs. */
sealed interface ShellState {
    data object Loading : ShellState
    data object Choose : ShellState
    data object Ready : ShellState
}

class ShellViewModel(private val settings: Settings) : ViewModel() {
    // A read that fails opens the tabs: Start's own failed state (0.58) is where a broken database is shown.
    val state: StateFlow<ShellState> = settings.modules()
        .map { if (it == null) ShellState.Choose else ShellState.Ready }
        .catch { emit(ShellState.Ready) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ShellState.Loading)

    // NonCancellable: Back right after "Step forward" ends this ViewModel; the choice is recorded all the same.
    fun choose(modules: Set<Module>) {
        viewModelScope.launch { withContext(NonCancellable) { settings.chooseModules(modules) } }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { ShellViewModel(container.settings) }
        }
    }
}
