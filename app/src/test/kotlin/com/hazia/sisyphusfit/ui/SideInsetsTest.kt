package com.hazia.sisyphusfit.ui

import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.core.graphics.Insets
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hazia.sisyphusfit.MainActivity
import com.hazia.sisyphusfit.TestContainerRule
import com.hazia.sisyphusfit.core.Module
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/** Landscape with three-button navigation: the buttons sit at a side, and nothing the person taps goes under them. */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36], qualifiers = "w756dp-h384dp-land-xxhdpi")
class SideInsetsTest {
    @get:Rule(order = 0) val container = TestContainerRule(chosen = null)
    @get:Rule(order = 1) val rule = createAndroidComposeRule<MainActivity>()

    private val bar = 48.dp

    private fun navigationAtTheRight() {
        val px = (bar.value * rule.activity.resources.displayMetrics.density).toInt()
        val insets = WindowInsetsCompat.Builder()
            .setInsets(WindowInsetsCompat.Type.navigationBars(), Insets.of(0, 0, px, 0))
            .setVisible(WindowInsetsCompat.Type.navigationBars(), true)
            .build()
        rule.runOnUiThread { ViewCompat.dispatchApplyWindowInsets(rule.activity.window.decorView, insets) }
        rule.waitForIdle()
    }

    @Test fun theChoiceStaysClearOfTheButtons() {
        rule.waitUntil(5_000) { rule.onAllNodesWithText("Start your journey").fetchSemanticsNodes().isNotEmpty() }
        navigationAtTheRight()
        val right = rule.onRoot().getUnclippedBoundsInRoot().right - bar
        Module.entries.forEach { m ->
            val card = rule.onNodeWithTag("module:${m.name}").getUnclippedBoundsInRoot()
            assertTrue("${m.name} card ends at ${card.right}, buttons start at $right", card.right <= right)
        }
    }

    @Test fun aTabStaysClearOfTheButtons() {
        rule.waitUntil(5_000) { rule.onAllNodesWithText("Start your journey").fetchSemanticsNodes().isNotEmpty() }
        rule.onAllNodesWithText("Step forward").onFirst().performClick()
        rule.waitUntil(5_000) { rule.onAllNodesWithTag("root:0").fetchSemanticsNodes().isNotEmpty() }
        navigationAtTheRight()
        val right = rule.onRoot().getUnclippedBoundsInRoot().right - bar
        val root = rule.onNodeWithTag("root:0").getUnclippedBoundsInRoot()
        assertTrue("Start's root ends at ${root.right}, buttons start at $right", root.right <= right)
    }
}
