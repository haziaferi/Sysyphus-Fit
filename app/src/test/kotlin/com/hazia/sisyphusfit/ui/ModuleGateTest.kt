package com.hazia.sisyphusfit.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hazia.sisyphusfit.MainActivity
import com.hazia.sisyphusfit.TestContainerRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/** First launch (SPEC 0.18): the choice comes before the tabs, once. */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36], qualifiers = "w384dp-h756dp-xxhdpi")
class ModuleGateTest {
    @get:Rule(order = 0) val container = TestContainerRule(chosen = null)
    @get:Rule(order = 1) val rule = createAndroidComposeRule<MainActivity>()

    private fun stored() = runBlocking { container.db.settingDao().value("modules").first() }

    // The setting is read and written off the main thread, which Compose's idling does not wait for.
    private fun waitFor(tag: String) = rule.waitUntil(5_000) { rule.onAllNodesWithTag(tag).fetchSemanticsNodes().isNotEmpty() }

    @Before fun opened() = rule.waitUntil(5_000) { rule.onAllNodesWithText("Start your journey").fetchSemanticsNodes().isNotEmpty() }

    private fun stepForward() {
        rule.onNodeWithText("Step forward").performClick()
        waitFor("root:0")
    }

    @Test fun firstLaunchShowsTheChoiceWithoutTabs() {
        rule.onNodeWithText("Start your journey").assertIsDisplayed()
        rule.onNode(hasText("This week") and isSelectable()).assertDoesNotExist()
    }

    @Test fun steppingForwardRecordsTheChoiceAndOpensStart() {
        rule.onNodeWithTag("module:STRENGTH").performClick()
        stepForward()
        rule.onNodeWithTag("root:0").assertIsDisplayed()
        assertEquals("STRENGTH", stored())
    }

    @Test fun steppingForwardWithNoneOpensStart() {
        stepForward()
        rule.onNodeWithTag("root:0").assertIsDisplayed()
        assertEquals("", stored())
    }

    // Robolectric finishes the activity; a phone moves the task to the back instead.
    @Test fun backOnTheChoiceLeavesWithoutSaving() {
        val activity = rule.activity
        rule.runOnUiThread { activity.onBackPressedDispatcher.onBackPressed() }
        rule.waitForIdle()
        assertTrue(activity.isFinishing)
        assertNull(stored())
    }
}
