package com.hazia.sisyphusfit.ui.modules

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.height
import androidx.compose.ui.unit.width
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hazia.sisyphusfit.core.Module
import com.hazia.sisyphusfit.ui.theme.SisyphusTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

/** First launch (SPEC 0.18): the five modules, all unticked, chosen by tapping their cards. */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36], qualifiers = "w384dp-h756dp-450dpi")
class ModuleChoiceScreenTest {
    @get:Rule val rule = createAndroidComposeRule<ComponentActivity>()

    private var chosen: Set<Module>? = null

    private fun show(mark: IncludesMark = IncludesMark.Star) =
        rule.setContent { SisyphusTheme { ModuleChoiceScreen(onStepForward = { chosen = it }, mark = mark) } }

    private fun card(m: Module) = rule.onNodeWithTag("module:${m.name}")

    @Test fun showsTheFiveModulesUntickedInSpecOrder() {
        show()
        val tops = Module.entries.map { m ->
            card(m).assertIsOff()
            card(m).getUnclippedBoundsInRoot().top
        }
        assertEquals(tops.sortedBy { it.value }, tops)
        rule.onNodeWithText("Start your journey").assertExists()
        rule.onNodeWithText("Choose which paths you want to follow. The other choices will still be available in Settings.").assertExists()
    }

    @Test fun tappingACardTicksItAndAgainUnticks() {
        show()
        card(Module.STAMINA).performClick().assertIsOn()
        card(Module.STAMINA).performClick().assertIsOff()
    }

    @Test fun cardsAreTogglesForTalkBack() {
        show()
        Module.entries.forEach { m ->
            card(m).assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Checkbox))
            val text = ModuleTexts.getValue(m)
            val merged = card(m).fetchSemanticsNode().config.getOrElse(SemanticsProperties.Text) { emptyList() }.map { it.text }
            assertTrue(m.name, text.name in merged && text.line in merged)
        }
    }

    @Test fun theMarkOpensIncludesWithoutTicking() {
        show()
        rule.onNodeWithTag("includes:STRENGTH").performClick()
        rule.onNodeWithTag("includes-card").assertExists()
        rule.onNodeWithText("Includes: customizable routines, rest timer, plank timer and tracking progress").assertExists()
        card(Module.STRENGTH).assertIsOff()
    }

    @Test fun onlyOneIncludesOpenAtATime() {
        show()
        rule.onNodeWithTag("includes:STRENGTH").performClick()
        rule.onNodeWithTag("includes-scrim").performClick()
        rule.onNodeWithTag("includes:STAMINA").performClick()
        rule.onAllNodesWithTag("includes-card").assertCountEquals(1)
        rule.onNodeWithText(ModuleTexts.getValue(Module.STAMINA).includes).assertExists()
    }

    @Test fun includesClosesOnTheScrimAndOnBack() {
        show()
        rule.onNodeWithTag("includes:DAILY_MOVEMENT").performClick()
        rule.onNodeWithTag("includes-scrim").performClick()
        rule.onNodeWithTag("includes-card").assertDoesNotExist()
        rule.onNodeWithTag("includes:DAILY_MOVEMENT").performClick()
        rule.onNodeWithTag("includes-card").assertExists()
        rule.runOnUiThread { rule.activity.onBackPressedDispatcher.onBackPressed() }
        rule.onNodeWithTag("includes-card").assertDoesNotExist()
        assertTrue("Back closes the card, not the screen", !rule.activity.isFinishing)
    }

    @Test fun fourModulesSayTheyArriveLater() {
        show()
        rule.onNodeWithTag("later:STRENGTH").assertDoesNotExist()
        Module.entries.filter { it != Module.STRENGTH }.forEach { rule.onNodeWithTag("later:${it.name}", useUnmergedTree = true).assertExists() }
        rule.onAllNodesWithText("Arrives in a later update", useUnmergedTree = true).assertCountEquals(4)
    }

    @Test fun stepForwardWithNoneGivesAnEmptySet() {
        show()
        rule.onNodeWithText("Step forward").performClick()
        assertEquals(emptySet<Module>(), chosen)
    }

    @Test fun stepForwardGivesTheTickedModules() {
        show()
        card(Module.STRENGTH).performClick()
        card(Module.BODY_MEASURES).performClick()
        rule.onNodeWithText("Step forward").performClick()
        assertEquals(setOf(Module.STRENGTH, Module.BODY_MEASURES), chosen)
    }

    @Test fun ticksSurviveRecreation() {
        val restore = StateRestorationTester(rule)
        restore.setContent { SisyphusTheme { ModuleChoiceScreen(onStepForward = { chosen = it }) } }
        card(Module.STAMINA).performClick()
        rule.onNodeWithTag("includes:STAMINA").performClick()
        restore.emulateSavedInstanceStateRestore()
        card(Module.STAMINA).assertIsOn()
        rule.onNodeWithTag("includes-card").assertDoesNotExist()
    }

    @Test fun markAndCheckboxKeepTheirDistance() {
        var current by mutableStateOf(IncludesMark.Star)
        rule.setContent { SisyphusTheme { ModuleChoiceScreen(onStepForward = {}, mark = current) } }
        IncludesMark.entries.forEach { mark ->
            current = mark
            rule.waitForIdle()
            Module.entries.forEach { m ->
                val markBox = rule.onNodeWithTag("includes:${m.name}").getUnclippedBoundsInRoot()
                val box = rule.onNodeWithTag("box:${m.name}", useUnmergedTree = true).getUnclippedBoundsInRoot()
                assertTrue("$mark ${m.name} mark width", markBox.width >= 42.dp)
                assertTrue("$mark ${m.name} mark height", markBox.height >= 42.dp)
                assertTrue("$mark ${m.name} gap", box.left - markBox.right >= 5.dp)
            }
        }
    }
}
