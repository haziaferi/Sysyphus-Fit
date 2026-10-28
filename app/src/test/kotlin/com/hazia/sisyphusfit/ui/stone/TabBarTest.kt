package com.hazia.sisyphusfit.ui.stone

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hazia.sisyphusfit.R
import com.hazia.sisyphusfit.ui.theme.SisyphusTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36], qualifiers = "w384dp-h756dp-xxhdpi")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class TabBarTest {
    @get:Rule val rule = createComposeRule()

    private val tabs = listOf(
        TabItem("Start", R.drawable.ic_tab_start), TabItem("This week", R.drawable.ic_tab_week),
        TabItem("History", R.drawable.ic_tab_history), TabItem("More", R.drawable.ic_tab_more),
    )

    // 0.54 / vl_tabfit: the floor is as wide as the current label, never under 60 dp, and runs 7 dp under it.
    @Test
    fun valleyFloorFitsLabel() {
        assertEquals(60f, valleyTarget(centre = 50f, labelWidth = 40f, labelBottom = 55f, density = 1f).floor, 0.01f)
        val wide = valleyTarget(centre = 50f, labelWidth = 80f, labelBottom = 55f, density = 1f)
        assertEquals(80f, wide.floor, 0.01f)
        assertEquals(62f, wide.floorY, 0.01f)
    }

    // The line runs at 1 dp outside the walls (32 dp across) and on the floor between them.
    @Test
    fun valleyLineShape() {
        val v = Valley(centre = 200f, floor = 60f, floorY = 69f)
        val pts = valleyPoints(v, density = 1f)
        assertEquals(1f, valleyYAt(pts, 200f - 30f - 32f - 1f), 0.01f)
        assertEquals(69f, valleyYAt(pts, 200f), 0.01f)
        assertEquals(1f, valleyYAt(pts, 200f + 30f + 32f + 5f), 0.01f)
    }

    @Test
    fun valleyJumpsWithReducedMotion() {
        rule.mainClock.autoAdvance = false
        var picked = 0
        rule.setContent {
            var selected by remember { mutableIntStateOf(0) }
            SisyphusTheme { TabBar(tabs, selected, { selected = it; picked = it }, reducedMotion = true) }
        }
        rule.mainClock.advanceTimeByFrame()
        val before = rule.onNodeWithTag("tabbar").fetchSemanticsNode().config.getOrNull(ValleyCentre)!!
        rule.onNodeWithText("History", useUnmergedTree = true).performClick()
        rule.mainClock.advanceTimeByFrame()
        rule.mainClock.advanceTimeByFrame()
        val after = rule.onNodeWithTag("tabbar").fetchSemanticsNode().config.getOrNull(ValleyCentre)!!
        rule.mainClock.advanceTimeBy(1000)
        val settled = rule.onNodeWithTag("tabbar").fetchSemanticsNode().config.getOrNull(ValleyCentre)!!
        assertEquals(2, picked)
        assertTrue("moved from $before", settled > before + 100f)
        assertEquals("already there two frames in", settled, after, 0.5f)
    }

    // 0.53 / valley-motion: 250 ms, cubic-bezier(0.2, 0, 0, 1); one frame in, the valley is on its way.
    @Test
    fun valleySlidesOtherwise() {
        rule.mainClock.autoAdvance = false
        rule.setContent {
            var selected by remember { mutableIntStateOf(0) }
            SisyphusTheme { TabBar(tabs, selected, { selected = it }, reducedMotion = false) }
        }
        rule.mainClock.advanceTimeByFrame()
        val start = centre()
        rule.onNodeWithText("History", useUnmergedTree = true).performClick()
        rule.mainClock.advanceTimeBy(48)
        val mid = centre()
        rule.mainClock.advanceTimeBy(400)
        val end = centre()
        assertTrue("on its way: $start < $mid < $end", mid > start + 1f && mid < end - 1f)
    }

    private fun centre() = rule.onNodeWithTag("tabbar").fetchSemanticsNode().config.getOrNull(ValleyCentre)!!

    // Review Focus 5: edge to edge, the bar sits above the navigation bar.
    @Test
    fun tabBarAboveNavigationInset() {
        rule.setContent { SisyphusTheme { TabBar(tabs, 0, {}, insets = WindowInsets(bottom = 48.dp)) } }
        val bar = rule.onNodeWithTag("tabbar").getUnclippedBoundsInRoot()
        val label = rule.onNodeWithText("Start", useUnmergedTree = true).getUnclippedBoundsInRoot()
        assertTrue("label ${label.bottom} above the inset (bar ${bar.bottom})", label.bottom <= bar.bottom - 48.dp)
    }

    @Test
    fun sheetDismissesOnScrimTap() {
        var dismissed = false
        rule.setContent { SisyphusTheme { BottomSheet(visible = true, onDismiss = { dismissed = true }) { } } }
        rule.onNodeWithTag("scrim").performClick()
        assertTrue(dismissed)
    }
}
