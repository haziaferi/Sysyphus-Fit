package com.hazia.sisyphusfit.ui.stone

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertHeightIsEqualTo
import androidx.compose.ui.test.assertWidthIsEqualTo
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hazia.sisyphusfit.R
import com.hazia.sisyphusfit.ui.theme.SisyphusTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@Config(sdk = [36], qualifiers = "xxhdpi")
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class StoneIconTest {
    @get:Rule val rule = createComposeRule()

    @Test
    fun iconIs24dp() {
        rule.setContent { SisyphusTheme { StoneIcon(R.drawable.ic_minus, Modifier.testTag("i")) } }
        rule.onNodeWithTag("i").assertWidthIsEqualTo(24.dp).assertHeightIsEqualTo(24.dp)
    }

    // 0.53 "m-icons-lit": a line of carved-light just under the ink, so the icon reads as cut into the surface.
    @Test
    fun litEdgeShowsUnderTheInk() {
        rule.setContent { SisyphusTheme { StoneIcon(R.drawable.ic_minus, Modifier.testTag("i"), tint = Color.Black) } }
        val img = rule.onNodeWithTag("i").captureToImage()
        val px = IntArray(img.width * img.height).also { img.readPixels(it) }
        val x = img.width / 2
        val column = (0 until img.height).map { px[it * img.width + x] }
        val lastInk = column.indexOfLast { it == Color.Black.toArgb() }
        assertTrue("ink found", lastInk > 0)
        val below = column[lastInk + 1]
        assertTrue("a light pixel under the ink (was ${Integer.toHexString(below)})", Color(below).red > 0.5f)
    }
}
