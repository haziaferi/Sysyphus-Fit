package com.hazia.sisyphusfit.ui.theme

import android.graphics.Color
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge

/**
 * Edge to edge, the app's way (owner 2026-10-28): the band behind Android's three navigation buttons takes the app's own
 * colour, as other apps do. The bar is transparent with dark buttons and Android's contrast scrim is off, so the surface
 * the app draws there shows through: the tab bar's on tab screens, the bottom zone's on doing screens.
 */
fun ComponentActivity.stoneEdgeToEdge() {
    enableEdgeToEdge(navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT))
    window.isNavigationBarContrastEnforced = false
}
