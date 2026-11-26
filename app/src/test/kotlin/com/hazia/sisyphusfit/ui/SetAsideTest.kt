package com.hazia.sisyphusfit.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hazia.sisyphusfit.MainActivity
import com.hazia.sisyphusfit.TestContainerRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/** SPEC:R122: a damaged file set aside is told before anything else, and "Continue" goes on to the module choice. */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36], qualifiers = "w384dp-h756dp-xxhdpi")
class SetAsideTest {
    @get:Rule(order = 0) val container = TestContainerRule(chosen = null, recovered = true)
    @get:Rule(order = 1) val rule = createAndroidComposeRule<MainActivity>()

    private fun waitForText(text: String) = rule.waitUntil(5_000) { rule.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty() }

    @Before fun opened() = waitForText("Your data was damaged")

    @Test fun itSaysSoBeforeTheChoice() {
        rule.onNodeWithText(
            "It couldn't be read, so it was set aside on your phone, not deleted. The app starts again from the beginning; " +
                "restoring a backup comes in a later update.",
        ).assertIsDisplayed()
        rule.onNodeWithText("Start your journey").assertDoesNotExist()
    }

    @Test fun continueGoesOnToTheChoice() {
        rule.onNodeWithText("Continue").performClick()
        waitForText("Start your journey")
        rule.onNodeWithText("Your data was damaged").assertDoesNotExist()
    }
}
