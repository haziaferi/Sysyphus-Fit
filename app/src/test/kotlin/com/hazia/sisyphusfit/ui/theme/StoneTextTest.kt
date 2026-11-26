package com.hazia.sisyphusfit.ui.theme

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.BasicText
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.getUnclippedBoundsInRoot
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

/** CSS line boxes: a line is exactly its token's line height, even where Sisifo's glyphs are taller (they overflow). */
@RunWith(AndroidJUnit4::class)
@Config(sdk = [36])
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class StoneTextTest {
    @get:Rule val rule = createComposeRule()

    private fun height(tag: String) = rule.onNodeWithTag(tag).getUnclippedBoundsInRoot().let { it.bottom - it.top }.value

    @Test
    fun lineBoxIsTheTokenLine() {
        val t = AppType(Tokens.Type.Balanced)
        rule.setContent {
            Column {
                StoneText("60", Modifier.testTag("num"), t.setNumber)
                StoneText("Bench press", Modifier.testTag("ex"), t.exercise)
                StoneText("Body text", Modifier.testTag("body"), t.body)
                StoneText("Overhead press\nwith a pause", Modifier.testTag("two"), t.heading)
            }
        }
        assertEquals(44f, height("num"), 0.5f)
        assertEquals(32f, height("ex"), 0.5f)
        assertEquals(24f, height("body"), 0.5f)
        assertEquals(56f, height("two"), 0.5f)
    }

    // At a large font size Android scales sp on a nonlinear curve; Compose keeps each line in proportion to its text
    // (line / size), so the box must too, or wrapped lines overlap the text below (found on the module choice at 2.0).
    @Test
    fun lineBoxFollowsTheTextAtLargeFontSizes() {
        RuntimeEnvironment.setFontScale(2f)
        val t = AppType(Tokens.Type.Balanced)
        rule.setContent {
            Column {
                StoneText("Creating mindful\ninterludes", Modifier.testTag("stone"), t.body)
                BasicText("Creating mindful\ninterludes", Modifier.testTag("plain"), style = t.body)
            }
        }
        assertEquals(height("plain"), height("stone"), 0.5f)
    }

    // SPEC:R56: on Android 11 (the 6T) wrapped lines keep the plain text's spacing at 1.3, as on Android 14; the box
    // rounds once (2 x 30.3 = 61) where Compose rounds per line (62), so they may differ by 1 dp.
    @Test
    @Config(sdk = [30])
    fun lineBoxFollowsTheTextOnAndroid11() {
        RuntimeEnvironment.setFontScale(1.3f)
        val t = AppType(Tokens.Type.Balanced)
        rule.setContent {
            Column {
                StoneText("Creating mindful\ninterludes", Modifier.testTag("stone"), t.body)
                BasicText("Creating mindful\ninterludes", Modifier.testTag("plain"), style = t.body)
            }
        }
        assertEquals(height("plain"), height("stone"), 1f)
    }
}
