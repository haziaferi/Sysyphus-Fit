package com.hazia.sisyphusfit.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hazia.sisyphusfit.MainActivity
import com.hazia.sisyphusfit.TestContainerRule
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

private const val LINE =
    "It couldn't be read, so it was set aside on your phone, not deleted. Your old data is kept and can be restored once " +
        "backups arrive in a later update. Until then the app starts again from the beginning."

/** SPEC:R122: a damaged file set aside is told before anything else, and "Start fresh" goes on to the module choice. */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36], qualifiers = "w384dp-h756dp-xxhdpi")
class SetAsideTest {
    @get:Rule(order = 0) val container = TestContainerRule(chosen = null, setAsideStamp = 42L)
    @get:Rule(order = 1) val rule = createAndroidComposeRule<MainActivity>()

    private fun waitForText(text: String) = rule.waitUntil(5_000) { rule.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty() }

    @Before fun opened() = waitForText("Your data was damaged")

    @Test fun itSaysSoBeforeTheChoice() {
        rule.onNodeWithText(LINE).assertIsDisplayed()
        rule.onNodeWithText("Start your journey").assertDoesNotExist()
    }

    // SPEC:R128: the message's last line ends within space.section (24 dp) of the bottom zone.
    @Test fun theMessageSitsJustAboveTheBar() {
        val line = rule.onNodeWithText(LINE).getUnclippedBoundsInRoot()
        val zone = rule.onNodeWithTag("piece:bottom-zone").getUnclippedBoundsInRoot()
        val gap = zone.top - line.bottom
        assertTrue("gap $gap", gap >= 0.dp && gap <= 24.dp)
    }

    @Test fun startFreshGoesOnToTheChoice() {
        rule.onNodeWithText("Start fresh").performClick()
        waitForText("Start your journey")
        rule.onNodeWithText("Your data was damaged").assertDoesNotExist()
    }
}
