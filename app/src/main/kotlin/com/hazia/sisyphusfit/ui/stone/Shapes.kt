package com.hazia.sisyphusfit.ui.stone

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import com.hazia.sisyphusfit.ui.theme.CornerRadii
import com.hazia.sisyphusfit.ui.theme.PebbleRadii
import com.hazia.sisyphusfit.ui.theme.Tokens

/** One corner's radii in px: horizontal and vertical (CSS elliptical corners). */
data class Corner(val h: Float, val v: Float)

/**
 * CSS `border-radius` resolved for a box (CSS Backgrounds 3, 5.5): corners in CSS order (top-left, top-right,
 * bottom-right, bottom-left); when two adjacent radii exceed their side, all are scaled down by the same factor.
 */
fun cssCorners(size: Size, radii: PebbleRadii): List<Corner> =
    reduce(size, (0..3).map { Corner(radii.horizontal[it] / 100f * size.width, radii.vertical[it] / 100f * size.height) })

fun cssCorners(size: Size, radii: CornerRadii, density: Float): List<Corner> =
    reduce(size, listOf(radii.topLeft, radii.topRight, radii.bottomRight, radii.bottomLeft).map { Corner(it.value * density, it.value * density) })

private fun reduce(size: Size, c: List<Corner>): List<Corner> {
    val f = minOf(
        1f,
        ratio(size.width, c[0].h + c[1].h), ratio(size.width, c[3].h + c[2].h),
        ratio(size.height, c[0].v + c[3].v), ratio(size.height, c[1].v + c[2].v),
    )
    return if (f < 1f) c.map { Corner(it.h * f, it.v * f) } else c
}

private fun ratio(side: Float, sum: Float) = if (sum > 0f) side / sum else 1f

/** The outline of a box with elliptical corners, as CSS draws it. */
fun cornersPath(size: Size, c: List<Corner>): Path = Path().apply {
    val (w, h) = size
    moveTo(c[0].h, 0f)
    lineTo(w - c[1].h, 0f)
    arcTo(Rect(w - 2 * c[1].h, 0f, w, 2 * c[1].v), -90f, 90f, false)
    lineTo(w, h - c[2].v)
    arcTo(Rect(w - 2 * c[2].h, h - 2 * c[2].v, w, h), 0f, 90f, false)
    lineTo(c[3].h, h)
    arcTo(Rect(0f, h - 2 * c[3].v, 2 * c[3].h, h), 90f, 90f, false)
    lineTo(0f, c[0].v)
    arcTo(Rect(0f, 0f, 2 * c[0].h, 2 * c[0].v), 180f, 90f, false)
    close()
}

/** A worn pebble (SPEC 0.53): each corner a slightly different elliptical radius. */
class PebbleShape(private val radii: PebbleRadii) : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density) =
        Outline.Generic(cornersPath(size, cssCorners(size, radii)))
}

/** Cut on the left, worn on the right (SPEC 0.53). */
object WornShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density) =
        Outline.Generic(cornersPath(size, cssCorners(size, Tokens.Radius.worn, density.density)))
}

// The worn side's light, as drawn in the decided mockups (polish round "ad-wear"): an ellipse 45% x 75% of the box,
// centred at 80% across and halfway down, fading from surface.worn-light to nothing.
private const val GLOW_CX = 0.80f
private const val GLOW_CY = 0.50f
private const val GLOW_RX = 0.45f
private const val GLOW_RY = 0.75f

private class WornGlow : ShaderBrush() {
    override fun createShader(size: Size): Shader {
        val rx = GLOW_RX * size.width
        val ry = GLOW_RY * size.height
        val centre = Offset(GLOW_CX * size.width, GLOW_CY * size.height)
        // A circle of radius rx, squeezed vertically about its centre into the ellipse.
        val shader = RadialGradientShader(centre, rx, listOf(Tokens.Colors.surfaceWornLight, Color.Transparent))
        val m = Matrix().apply {
            translate(centre.x, centre.y)
            scale(1f, ry / rx)
            translate(-centre.x, -centre.y)
        }
        return shader.also { it.setLocalMatrix(android.graphics.Matrix().apply { setValues(m.toAndroidValues()) }) }
    }
}

private fun Matrix.toAndroidValues() = floatArrayOf(
    values[Matrix.ScaleX], values[Matrix.SkewX], values[Matrix.TranslateX],
    values[Matrix.SkewY], values[Matrix.ScaleY], values[Matrix.TranslateY],
    values[Matrix.Perspective0], values[Matrix.Perspective1], values[Matrix.Perspective2],
)

/** A worn surface: module cards, week sections, the resume card and rows (SPEC 0.53). */
fun Modifier.worn(): Modifier = this
    .background(Tokens.Colors.surfaceWorn, WornShape)
    .drawBehind {
        drawPath(WornShape.createOutline(size, layoutDirection, this).path, WornGlow())
    }
