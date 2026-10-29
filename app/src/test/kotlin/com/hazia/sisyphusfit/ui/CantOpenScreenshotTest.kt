package com.hazia.sisyphusfit.ui

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage
import com.hazia.sisyphusfit.ui.theme.DensityMode
import com.hazia.sisyphusfit.ui.theme.SisyphusTheme
import com.hazia.sisyphusfit.ui.theme.Tokens
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

// As the gallery's goldens: a change over 0.02% of the image is a change.
private val options = RoborazziOptions(compareOptions = RoborazziOptions.CompareOptions(changeThreshold = 0.0002f))

/** The can't-open screen (0.60) in the 6T's app area (384 x 756 dp at 450 dpi, SPEC 0.48), at font 1.0 and 2.0. */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class CantOpenScreenshotTest {
    @get:Rule val rule = createComposeRule()

    private fun golden(scale: Float) {
        RuntimeEnvironment.setFontScale(scale)
        rule.setContent { SisyphusTheme(DensityMode.Balanced) { CantOpenScreen(onRetry = {}, Modifier.background(Tokens.Colors.surfacePage)) } }
        rule.onRoot().captureRoboImage("cant-open-balanced-${(scale * 100).toInt()}.png", roborazziOptions = options)
    }

    @Test @Config(sdk = [36], qualifiers = "w384dp-h756dp-450dpi") fun balanced100() = golden(1f)
    @Test @Config(sdk = [36], qualifiers = "w384dp-h756dp-450dpi") fun balanced200() = golden(2f)
}
