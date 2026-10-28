package com.hazia.sisyphusfit.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ThemeTest {
    @get:Rule val rule = createComposeRule()

    private fun <T> read(density: DensityMode = DensityMode.Balanced, block: @Composable () -> T): T {
        var out: T? = null
        rule.setContent { SisyphusTheme(density) { out = block() } }
        rule.waitForIdle()
        @Suppress("UNCHECKED_CAST")
        return out as T
    }

    private fun widthsOf(text: String, vararg styles: TextStyle): List<Dp> {
        rule.setContent { SisyphusTheme { Column { styles.forEachIndexed { i, s -> BasicText(text, Modifier.testTag("t$i"), style = s) } } } }
        return styles.indices.map { i -> rule.onNodeWithTag("t$i").getUnclippedBoundsInRoot().let { it.right - it.left } }
    }

    @Test
    fun compactSwapsSpaceAndType() {
        val (gutter, body) = read(DensityMode.Compact) { Theme.space.gutter to Theme.type.body.fontSize }
        assertEquals(12.dp, gutter)
        assertEquals(14.sp, body)
    }

    @Test
    fun balancedIsTheDefault() {
        val (gutter, body) = read { Theme.space.gutter to Theme.type.body.fontSize }
        assertEquals(16.dp, gutter)
        assertEquals(16.sp, body)
    }

    @Test
    fun rowTitleHasTnumAndCarve100() {
        val style = read { Theme.type.rowTitle }
        assertEquals("tnum", style.fontFeatureSettings)
        assertEquals(sisifo(500, 100f), style.fontFamily)
    }

    @Test
    fun labelHasCarve0() {
        assertEquals(sisifo(400, 0f), read { Theme.type.label }.fontFamily)
    }

    // Review Focus 4: a family that fell back to the default instance (wght 400, CARV 0) would pass the tests above.
    @Test
    fun variationIsApplied() {
        val base = TextStyle(fontSize = 40.sp, fontFeatureSettings = "tnum")
        val (regular, bold) = widthsOf("0000", base.copy(fontFamily = sisifo(400, 0f)), base.copy(fontFamily = sisifo(700, 0f)))
        assertTrue("tabular 0000 at wght 700 ($bold) is wider than at 400 ($regular)", bold > regular)
    }

    // SPEC 0.49: at font size 2.0 Android 14+ scales sp on its nonlinear curve (16 -> 28 dp), not linearly (32).
    @Test
    fun fontScale2UsesNonlinearCurve() {
        RuntimeEnvironment.setFontScale(2f)
        val dp = read { with(LocalDensity.current) { Theme.type.body.fontSize.toDp() } }
        assertEquals(28f, dp.value, 0.5f)
    }
}
