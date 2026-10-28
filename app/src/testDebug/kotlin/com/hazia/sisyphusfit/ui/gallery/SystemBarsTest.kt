package com.hazia.sisyphusfit.ui.gallery

import android.graphics.Color
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.annotation.Config

/**
 * Owner 2026-10-28: the band behind Android's three navigation buttons takes the app's own colour, as other apps do.
 * Android's contrast scrim is off and the bar transparent, so the surface the app draws there (the tab bar, the bottom
 * zone) shows through.
 */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class SystemBarsTest {
    @Test
    fun navigationBandShowsTheApp() {
        val activity = Robolectric.buildActivity(GalleryActivity::class.java).setup().get()
        @Suppress("DEPRECATION")
        assertEquals(Color.TRANSPARENT, activity.window.navigationBarColor)
        assertFalse(activity.window.isNavigationBarContrastEnforced)
    }
}
