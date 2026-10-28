package com.hazia.sisyphusfit

import android.graphics.Color
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.annotation.Config

/** 0.56: the band behind Android's navigation buttons shows the app's own surface. */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
class MainActivityTest {
    @get:Rule val container = TestContainerRule()

    @Test
    fun navigationBandShowsTheApp() {
        val activity = Robolectric.buildActivity(MainActivity::class.java).setup().get()
        @Suppress("DEPRECATION")
        assertEquals(Color.TRANSPARENT, activity.window.navigationBarColor)
        assertFalse(activity.window.isNavigationBarContrastEnforced)
    }
}
