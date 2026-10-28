package com.hazia.sisyphusfit.ui.stone

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeDown
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hazia.sisyphusfit.ui.AppTabs
import com.hazia.sisyphusfit.ui.gallery.PieceSamples
import com.hazia.sisyphusfit.ui.theme.DensityMode
import com.hazia.sisyphusfit.ui.theme.SisyphusTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/** Each test reproduces a finding of the whole-branch review (2026-10-28). */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36], qualifiers = "w384dp-h756dp-xxhdpi")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ReviewFixesTest {
    @get:Rule val rule = createAndroidComposeRule<ComponentActivity>()

    // Important 1: the wrap decision must follow the font size, not only the screen density.
    @Test
    fun tabLabelsWrapWhenTheFontSizeChanges() {
        var big by mutableStateOf(false)
        rule.setContent {
            val ctx = LocalContext.current
            val config = LocalConfiguration.current
            val base = LocalDensity.current
            val d = if (!big) base else Density(ctx.createConfigurationContext(android.content.res.Configuration(config).apply { fontScale = 2f }))
            CompositionLocalProvider(LocalDensity provides d) { SisyphusTheme { TabBar(AppTabs, 0, {}) } }
        }
        val before = rule.onNodeWithText("This week", useUnmergedTree = true).getUnclippedBoundsInRoot()
        big = true
        rule.waitForIdle()
        val after = rule.onNodeWithText("This week", useUnmergedTree = true).getUnclippedBoundsInRoot()
        assertTrue("wrapped: ${before.bottom - before.top} -> ${after.bottom - after.top}", (after.bottom - after.top).value > 2.4f * (before.bottom - before.top).value)
    }

    // Important 2: every pressable has a name TalkBack can read.
    @Test
    fun everyPressableHasAName() {
        rule.setContent { SisyphusTheme { PieceSamples() } }
        val pressable = rule.onAllNodes(
            SemanticsMatcher.keyIsDefined(SemanticsActions.OnClick), useUnmergedTree = false,
        ).fetchSemanticsNodes()
        val unnamed = pressable.filter {
            it.config.getOrNull(SemanticsProperties.Text).isNullOrEmpty() &&
                it.config.getOrNull(SemanticsProperties.ContentDescription).isNullOrEmpty()
        }
        assertTrue("unnamed: ${unnamed.map { it.config.getOrNull(SemanticsActions.OnClick)?.label ?: it.config.getOrNull(SemanticsProperties.Role) }}", unnamed.isEmpty())
    }

    // Important 3: system Back closes an open sheet before it leaves the screen.
    @Test
    fun backClosesTheSheet() {
        var open by mutableStateOf(true)
        rule.setContent { SisyphusTheme { BottomSheet(open, onDismiss = { open = false }) { } } }
        rule.runOnUiThread { rule.activity.onBackPressedDispatcher.onBackPressed() }
        rule.waitForIdle()
        assertTrue("closed", !open)
        assertTrue("activity still there", !rule.activity.isFinishing)
    }

    // Important 4: with navigation on the side (landscape, three buttons) the tab bar keeps clear of it.
    @Test
    fun tabBarKeepsClearOfASideNavigationBar() {
        rule.setContent { SisyphusTheme { TabBar(AppTabs, 3, {}, insets = WindowInsets(right = 48.dp, bottom = 0.dp)) } }
        val bar = rule.onNodeWithTag("tabbar").getUnclippedBoundsInRoot()
        val more = rule.onNodeWithText("More", useUnmergedTree = true).getUnclippedBoundsInRoot()
        assertTrue("More (${more.right}) left of the inset (${bar.right - 48.dp})", more.right <= bar.right - 48.dp)
    }

    @Test
    fun bottomZoneKeepsClearOfTheNavigationBar() {
        rule.setContent {
            SisyphusTheme { BottomZone(insets = WindowInsets(left = 48.dp, bottom = 48.dp)) { MainBar("Done", onClick = {}) } }
        }
        val zone = rule.onNodeWithTag("piece:bottom-zone").getUnclippedBoundsInRoot()
        val bar = rule.onNodeWithTag("piece:main-bar").getUnclippedBoundsInRoot()
        assertTrue("above the bottom inset", bar.bottom <= zone.bottom - 48.dp)
        assertTrue("right of the side inset", bar.left >= zone.left + 48.dp)
    }

    // Minor 5, re-graded: SPEC 0.47, the chip keeps its 48 dp touch target, compact too.
    @Test
    fun chipKeeps48dpAtCompact() {
        rule.setContent { SisyphusTheme(DensityMode.Compact) { Chip("Training frequency", onClick = {}) } }
        val node = rule.onNodeWithText("Training frequency").fetchSemanticsNode()
        val h = with(rule.density) { node.touchBoundsInRoot.height.toDp() }
        assertTrue("$h", h >= 48.dp)
    }

    // Minor 8, re-graded: the drag offset is written as the finger moves (no late snap can overtake the spring-back);
    // a drag past a quarter of the sheet closes it. The fling's velocity reads 0 under Robolectric, so it is walked on the 6T.
    @Test
    fun aDragPastAQuarterClosesTheSheet() {
        var open by mutableStateOf(true)
        rule.setContent { SisyphusTheme { BottomSheet(open, onDismiss = { open = false }) { Box(Modifier.width(300.dp).height(200.dp)) } } }
        rule.onNodeWithTag("piece:grab", useUnmergedTree = true).performTouchInput {
            swipeDown(startY = centerY, endY = centerY + 150 * density, durationMillis = 300)
        }
        rule.waitForIdle()
        assertTrue("closed", !open)
    }

    // Minor 9, re-graded (a crash): a worn surface with no width draws nothing instead of throwing.
    @Test
    fun wornSurfaceAtZeroWidthDoesNotCrash() {
        rule.setContent { SisyphusTheme { Box(Modifier.width(0.dp)) { WornCard { } } } }
        rule.waitForIdle()
    }
}
