package com.hazia.sisyphusfit

import android.app.Application
import com.hazia.sisyphusfit.data.openSisyphusDatabase

class SisyphusApp : Application() {
    /** Built with the app, opening nothing until a screen asks. A test sets its own before an activity starts. */
    var container: AppContainer = AppContainer({ openSisyphusDatabase(this) })
}
