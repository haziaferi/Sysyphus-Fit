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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

/**
 * What "Start or log" (SPEC:R83) shows: a session to resume (GL1, SPEC:R34) and the modules the person chose; [failed] when
 * the database could not be read, for the screen to say so.
 */
data class StartState(
    val loading: Boolean = true,
    val resume: OpenSession? = null,
    val modules: Set<Module> = emptySet(),
    val failed: Boolean = false,
)

class StartViewModel(sessions: SessionStore, settings: Settings) : ViewModel() {
    // Whenever Start's state starts being collected (it opens, or returns after more than 5 s away), an idle session from
    // an earlier day is closed first (SPEC:R96), so it is not offered for resume.
    val state: StateFlow<StartState> = flow {
        sessions.closeIfIdle()
        emitAll(combine(sessions.open(), settings.modules()) { open, modules ->
            StartState(loading = false, resume = open, modules = modules.orEmpty())
        })
    }.catch { emit(StartState(loading = false, failed = true)) }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), StartState())

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer { StartViewModel(container.sessions, container.settings) }
        }
    }
}
