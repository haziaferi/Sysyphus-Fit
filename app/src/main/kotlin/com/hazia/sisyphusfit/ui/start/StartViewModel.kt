package com.hazia.sisyphusfit.ui.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.hazia.sisyphusfit.core.Module
import com.hazia.sisyphusfit.data.OpenSession
import com.hazia.sisyphusfit.data.SessionStore
import com.hazia.sisyphusfit.data.Settings
import com.hazia.sisyphusfit.ui.container
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

/** What "Start or log" (0.25) shows: a session to resume (GL1, 0.27) and the modules the person chose. */
data class StartState(val loading: Boolean = true, val resume: OpenSession? = null, val modules: Set<Module> = emptySet())

class StartViewModel(sessions: SessionStore, settings: Settings) : ViewModel() {
    val state: StateFlow<StartState> = combine(sessions.open(), settings.modules()) { open, modules ->
        StartState(loading = false, resume = open, modules = modules.orEmpty())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), StartState())

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { StartViewModel(container.sessions, container.settings) }
        }
    }
}
