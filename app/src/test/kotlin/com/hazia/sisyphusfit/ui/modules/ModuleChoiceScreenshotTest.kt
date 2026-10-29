package com.hazia.sisyphusfit.ui.modules

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage
import com.hazia.sisyphusfit.core.Module
import com.hazia.sisyphusfit.ui.theme.DensityMode
import com.hazia.sisyphusfit.ui.theme.SisyphusTheme
import com.hazia.sisyphusfit.ui.theme.Tokens
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

// As the gallery's goldens: a change over 0.02% of the image is a change.
private val options = RoborazziOptions(compareOptions = RoborazziOptions.CompareOptions(changeThreshold = 0.0002f))

/** The module choice in the 6T's app area (384 x 756 dp at 450 dpi, SPEC 0.48), balanced and compact, font 1.0 and 2.0. */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ModuleChoiceScreenshotTest {
    @get:Rule val rule = createComposeRule()

    private fun show(density: DensityMode, scale: Float) {
        RuntimeEnvironment.setFontScale(scale)
        rule.setContent {
            SisyphusTheme(density) { ModuleChoiceScreen(onStepForward = {}, Modifier.background(Tokens.Colors.surfacePage)) }
        }
    }

    private fun golden(density: DensityMode, scale: Float) {
        show(density, scale)
        rule.onRoot().captureRoboImage("module-choice-${density.name.lowercase()}-${(scale * 100).toInt()}.png", roborazziOptions = options)
    }

    @Test @Config(sdk = [36], qualifiers = "w384dp-h756dp-450dpi") fun balanced100() = golden(DensityMode.Balanced, 1f)
    @Test @Config(sdk = [36], qualifiers = "w384dp-h756dp-450dpi") fun balanced200() = golden(DensityMode.Balanced, 2f)
    @Test @Config(sdk = [36], qualifiers = "w384dp-h756dp-450dpi") fun compact100() = golden(DensityMode.Compact, 1f)
    @Test @Config(sdk = [36], qualifiers = "w384dp-h756dp-450dpi") fun compact200() = golden(DensityMode.Compact, 2f)

    // 0.49, 0.54: at font 2.0 on the narrowest phone the bar stays whole on screen and every card scrolls into view.
    @Test @Config(sdk = [36], qualifiers = "w360dp-h756dp-450dpi")
    fun stepForwardStaysOnScreenAt2x() {
        show(DensityMode.Balanced, 2f)
        val root = rule.onRoot().getUnclippedBoundsInRoot()
        val bar = rule.onNodeWithText("Step forward").getUnclippedBoundsInRoot()
        assertTrue("bar inside the screen: $bar in $root", bar.top >= root.top && bar.bottom <= root.bottom)
        rule.onNodeWithTag("module:BODY_MEASURES").performScrollTo().assertIsDisplayed()
        val last = rule.onNodeWithTag("module:BODY_MEASURES").getUnclippedBoundsInRoot()
        assertTrue("last card above the bar", last.bottom <= bar.top)
    }

    // 0.54's reflow: when a name would wrap beside the chip, every card puts the chip and box under its text, right-aligned.
    @Test @Config(sdk = [36], qualifiers = "w360dp-h756dp-450dpi")
    fun atLargeFontTheChipGoesUnderTheText() {
        show(DensityMode.Balanced, 2f)
        Module.entries.forEach { m ->
            val name = rule.onNodeWithText(ModuleTexts.getValue(m).line, useUnmergedTree = true).getUnclippedBoundsInRoot()
            val chip = rule.onNodeWithTag("includes:${m.name}").getUnclippedBoundsInRoot()
            assertTrue("${m.name}: chip under its line: chip $chip line $name", chip.top >= name.bottom - 1.dp)
        }
    }

    @Test @Config(sdk = [36], qualifiers = "w384dp-h756dp-450dpi")
    fun atNormalSizeTheChipStaysBeside() {
        show(DensityMode.Balanced, 1f)
        Module.entries.forEach { m ->
            val name = rule.onNodeWithText(ModuleTexts.getValue(m).name, useUnmergedTree = true).getUnclippedBoundsInRoot()
            val chip = rule.onNodeWithTag("includes:${m.name}").getUnclippedBoundsInRoot()
            assertTrue("${m.name}: chip beside the text", chip.left >= name.right)
        }
    }
}
