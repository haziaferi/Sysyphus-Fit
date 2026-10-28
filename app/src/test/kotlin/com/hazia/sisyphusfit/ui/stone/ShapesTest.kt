package com.hazia.sisyphusfit.ui.stone

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.unit.dp
import com.hazia.sisyphusfit.ui.theme.PebbleRadii
import com.hazia.sisyphusfit.ui.theme.ShadowSpec
import com.hazia.sisyphusfit.ui.theme.Tokens
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ShapesTest {
    private fun assertCorner(h: Float, v: Float, c: Corner) {
        assertEquals(h, c.h, 0.001f)
        assertEquals(v, c.v, 0.001f)
    }

    // CSS: horizontal percentages of the width, vertical of the height, corners top-left, top-right, bottom-right, bottom-left.
    @Test
    fun pebbleMatchesCssAtSquare() {
        val c = cssCorners(Size(56f, 56f), Tokens.Radius.pebbleA)
        assertCorner(0.49f * 56, 0.52f * 56, c[0])
        assertCorner(0.51f * 56, 0.46f * 56, c[1])
        assertCorner(0.56f * 56, 0.54f * 56, c[2])
        assertCorner(0.44f * 56, 0.48f * 56, c[3])
    }

    @Test
    fun pebbleKeepsPercentagesAtAWidePill() {
        val c = cssCorners(Size(200f, 50f), Tokens.Radius.pebblePill)
        assertCorner(0.52f * 200, 0.50f * 50, c[0])
        assertCorner(0.50f * 200, 0.46f * 50, c[2])
    }

    // CSS Backgrounds 3, 5.5: when adjacent radii exceed their side, every radius is scaled by the smallest side / sum.
    @Test
    fun pebbleReducesLikeCssWhenRadiiOverlap() {
        val over = PebbleRadii(listOf(60f, 60f, 50f, 50f), listOf(50f, 50f, 50f, 50f))
        val c = cssCorners(Size(100f, 25f), over)
        val f = 100f / 120f
        assertCorner(60f * f, 12.5f * f, c[0])
        assertCorner(50f * f, 12.5f * f, c[3])
    }

    @Test
    fun wornIsSquareLeft() {
        val c = cssCorners(Size(300f, 72f), Tokens.Radius.worn, density = 1f)
        assertCorner(0f, 0f, c[0])
        assertCorner(8f, 8f, c[1])
        assertCorner(8f, 8f, c[2])
        assertCorner(0f, 0f, c[3])
    }

    @Test
    fun cssShadowMapsToCompose() {
        val s = ShadowSpec(0.dp, 6.dp, 16.dp, 0.dp, Tokens.Colors.shadowFloatFar, inset = false).toShadow()
        assertEquals(16.dp, s.radius)
        assertEquals(6.dp, s.offset.y)
        assertTrue(Tokens.Elevation.edgeInset.single().inset)
        assertFalse(Tokens.Elevation.float.any { it.inset })
    }
}
