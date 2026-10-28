package com.hazia.sisyphusfit.ui.stone

import androidx.compose.foundation.layout.width
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.semantics.SemanticsActions
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.DpRect
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hazia.sisyphusfit.ui.gallery.PieceSamples
import com.hazia.sisyphusfit.ui.theme.DensityMode
import com.hazia.sisyphusfit.ui.theme.SisyphusTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36], qualifiers = "w384dp-h756dp-xxhdpi")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class PiecesTest {
    @get:Rule val rule = createComposeRule()

    private fun DpRect.h() = bottom - top
    private fun DpRect.w() = right - left

    // SPEC 0.21: touch areas at least 42 dp; the pieces use size.hit (48 / 44).
    @Test
    fun everyPressableIsAtLeast42dp() {
        rule.setContent { SisyphusTheme { PieceSamples() } }
        val pressable = rule.onAllNodes(SemanticsMatcher.keyIsDefined(SemanticsActions.OnClick)).fetchSemanticsNodes()
        assertTrue("pieces found: ${pressable.size}", pressable.size >= 10)
        pressable.forEach { n: SemanticsNode ->
            val r: Rect = n.touchBoundsInRoot
            val (w, h) = with(rule.density) { r.width.toDp() to r.height.toDp() }
            assertTrue("${n.config.getOrNull(SemanticsActions.OnClick)?.label ?: n.id} is $w x $h", w >= 42.dp && h >= 42.dp)
        }
    }

    @Test
    fun compactPressablesAreAtLeast42dp() {
        rule.setContent { SisyphusTheme(DensityMode.Compact) { PieceSamples() } }
        rule.onAllNodes(SemanticsMatcher.keyIsDefined(SemanticsActions.OnClick)).fetchSemanticsNodes().forEach { n ->
            val (w, h) = with(rule.density) { n.touchBoundsInRoot.width.toDp() to n.touchBoundsInRoot.height.toDp() }
            assertTrue("node ${n.id} is $w x $h", w >= 42.dp && h >= 42.dp)
        }
    }

    // 0.49: the main bar may wrap to two lines; it never cuts its label.
    @Test
    fun mainBarWrapsAtFontScale2() {
        RuntimeEnvironment.setFontScale(2f)
        rule.setContent { SisyphusTheme { MainBar("Done · set 2 of 3, then rest", onClick = {}, modifier = Modifier.width(300.dp)) } }
        val text = rule.onNodeWithText("Done · set 2 of 3, then rest", useUnmergedTree = true).getUnclippedBoundsInRoot()
        assertTrue("two lines: ${text.h()}", text.h() > 40.dp)
    }

    // 0.43, 0.49: the title may end in "…"; the clock and Finish never shorten.
    @Test
    fun headerKeepsClockAndFinishAtFontScale2() {
        RuntimeEnvironment.setFontScale(2f)
        rule.setContent {
            SisyphusTheme { HeaderRow("Tuesday morning, upper body and a long walk", clock = "12:40", onBack = {}, onFinish = {}) }
        }
        val clock = rule.onNodeWithText("12:40", useUnmergedTree = true).getUnclippedBoundsInRoot()
        val finish = rule.onNodeWithText("Finish", useUnmergedTree = true).getUnclippedBoundsInRoot()
        val root = rule.onNodeWithTag("header").getUnclippedBoundsInRoot()
        assertTrue("clock fully inside", clock.right <= finish.left && clock.w() > 30.dp)
        assertTrue("finish inside the header", finish.right <= root.right)
    }

    @Test
    fun headerTitleShowsInFullOnATap() {
        RuntimeEnvironment.setFontScale(2f)
        rule.setContent { SisyphusTheme { HeaderRow("Tuesday morning, upper body and a long walk", clock = "12:40", onBack = {}, onFinish = {}) } }
        val before = rule.onNodeWithTag("header-title", useUnmergedTree = true).getUnclippedBoundsInRoot().h()
        rule.onNodeWithTag("header-title", useUnmergedTree = true).performClick()
        val after = rule.onNodeWithTag("header-title", useUnmergedTree = true).getUnclippedBoundsInRoot().h()
        assertTrue("$before -> $after", after > before)
    }

    // 0.49: the value shrinks to fit between the steppers, not below the compact token (34 sp), then its unit moves under it.
    @Test
    fun stepperValueShrinksThenUnitMovesUnder() {
        RuntimeEnvironment.setFontScale(2f)
        rule.setContent {
            SisyphusTheme { Stepper("142.25", "kg", onMinus = {}, onPlus = {}, onTapValue = {}, modifier = Modifier.width(300.dp)) }
        }
        val value = rule.onNodeWithTag("stepper-value", useUnmergedTree = true).getUnclippedBoundsInRoot()
        val unit = rule.onNodeWithTag("stepper-unit", useUnmergedTree = true).getUnclippedBoundsInRoot()
        assertTrue("unit under the value: unit.top ${unit.top} >= value.bottom ${value.bottom}", unit.top >= value.bottom - 1.dp)
    }

    @Test
    fun stepperUnitBesideValueWhenItFits() {
        rule.setContent { SisyphusTheme { Stepper("60", "kg", onMinus = {}, onPlus = {}, onTapValue = {}) } }
        val value = rule.onNodeWithTag("stepper-value", useUnmergedTree = true).getUnclippedBoundsInRoot()
        val unit = rule.onNodeWithTag("stepper-unit", useUnmergedTree = true).getUnclippedBoundsInRoot()
        assertTrue("unit beside", unit.left >= value.right)
        assertEquals("value at its full size: 44 dp line", 44f, value.h().value, 1f)
    }
}
