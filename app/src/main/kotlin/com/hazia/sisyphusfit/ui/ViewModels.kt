package com.hazia.sisyphusfit.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import com.hazia.sisyphusfit.AppContainer
import com.hazia.sisyphusfit.SisyphusApp

/** The app's container, as a screen's ViewModel factory reaches it (SPEC 0.58: wired by hand). */
internal val CreationExtras.container: AppContainer
    get() = (checkNotNull(this[APPLICATION_KEY]) { "no Application in the creation extras" } as SisyphusApp).container
