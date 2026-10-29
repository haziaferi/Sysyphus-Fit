package com.hazia.sisyphusfit.ui

import android.os.Bundle
import android.os.Parcel
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isSelectable
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.hazia.sisyphusfit.MainActivity
import com.hazia.sisyphusfit.TestContainerRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.annotation.Config

/** The four tabs (0.29, 0.39) on Navigation-compose (0.58); the app opens on Start (0.25). */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36], qualifiers = "w384dp-h756dp-xxhdpi")
class AppShellTest {
    @get:Rule(order = 0) val container = TestContainerRule()
    @get:Rule(order = 1) val rule = createAndroidComposeRule<MainActivity>()

    private fun tab(label: String) = rule.onNode(hasText(label) and isSelectable())
    private fun tap(label: String) = tab(label).performClick().also { rule.waitForIdle() }
    private fun shows(root: Int) = rule.onNodeWithTag("root:$root").assertIsDisplayed()

    // The shell shows only the page while it reads the module choice off the main thread (0.18), which Compose's idling
    // does not wait for.
    private fun ready() = rule.waitUntil(5_000) { rule.onAllNodesWithTag("root:0").fetchSemanticsNodes().isNotEmpty() }

    @Before fun opened() = ready()

    private fun back() {
        rule.runOnUiThread { rule.activity.onBackPressedDispatcher.onBackPressed() }
        rule.waitForIdle()
    }

    @Test fun opensOnStart() {
        shows(0)
        tab("Start").assertIsSelected()
    }

    @Test fun eachTabShowsItsRoot() {
        listOf("This week" to 1, "History" to 2, "More" to 3, "Start" to 0).forEach { (label, root) ->
            tap(label)
            shows(root)
            tab(label).assertIsSelected()
        }
    }

    @Test fun backFromAnotherTabGoesToStart() {
        tap("History")
        back()
        shows(0)
        tab("Start").assertIsSelected()
    }

    // Robolectric finishes the activity; a phone moves the task to the back instead.
    @Test fun backOnStartLeavesTheApp() {
        val activity = rule.activity
        back()
        assertTrue(activity.isFinishing)
    }

    @Test fun rotationKeepsTheTab() {
        tap("History")
        rule.activityRule.scenario.recreate()
        rule.waitUntil(5_000) { rule.onAllNodesWithTag("root:2").fetchSemanticsNodes().isNotEmpty() }
        shows(2)
        tab("History").assertIsSelected()
    }

    // Process death as Robolectric allows (the spike's T7): the state goes through a Parcel's bytes, the activity
    // and its ViewModels are destroyed, and a new activity is built from the bundle.
    @Test fun processDeathKeepsTheTabAndBackStillLeadsToStart() {
        tap("History")
        val scenario = rule.activityRule.scenario
        scenario.moveToState(Lifecycle.State.CREATED)
        val saved = Bundle()
        scenario.onActivity { InstrumentationRegistry.getInstrumentation().callActivityOnSaveInstanceState(it, saved) }
        val bytes = Parcel.obtain().run { writeBundle(saved); marshall().also { recycle() } }
        val restored = Parcel.obtain().run {
            unmarshall(bytes, 0, bytes.size); setDataPosition(0)
            readBundle(MainActivity::class.java.classLoader).also { recycle() }
        }
        scenario.moveToState(Lifecycle.State.DESTROYED)

        val controller = Robolectric.buildActivity(MainActivity::class.java).setup(restored)
        rule.waitUntil(5_000) { rule.onAllNodesWithTag("root:2").fetchSemanticsNodes().isNotEmpty() }
        shows(2)
        tab("History").assertIsSelected()
        controller.get().onBackPressedDispatcher.onBackPressed()
        rule.waitForIdle()
        shows(0)
        // M1: and from Start, out of the app.
        controller.get().onBackPressedDispatcher.onBackPressed()
        assertTrue(controller.get().isFinishing)
        controller.pause().stop().destroy()
    }

    @Test fun startRootGetsItsStoresFromTheApp() {
        rule.waitUntil(5_000) { container.opens == 1 }
        assertEquals(false, container.openedOnMain)
    }
}
