package com.hazia.sisyphusfit.ui.stone

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import com.hazia.sisyphusfit.ui.theme.Tokens

enum class CarvedEdge { Top, Bottom }

/**
 * A line carved into the surface (SPEC 0.53): a band `stroke.carved` high, its upper half a dark lens (the cut), its lower
 * half a light lens (the lit edge), each thickest in the middle, as drawn in the decided mockups. [faded] fades both
 * ends over a fifth of the width (History's date spindle); [cut] is the dark lens's colour.
 */
fun Modifier.carvedLine(
    at: CarvedEdge = CarvedEdge.Bottom,
    faded: Boolean = false,
    cut: Color = Tokens.Colors.lineCarvedDark,
): Modifier = graphicsLayer { if (faded) compositingStrategy = CompositingStrategy.Offscreen }
    .drawWithContent {
        drawContent()
        val band = Tokens.Stroke.carved.toPx()
        val top = if (at == CarvedEdge.Top) 0f else size.height - band
        drawCarve(top, band, cut)
        if (faded) {
            drawRect(
                Brush.horizontalGradient(0f to Color.Transparent, 0.2f to Color.Black, 0.8f to Color.Black, 1f to Color.Transparent),
                topLeft = Offset(0f, top), size = size.copy(height = band), blendMode = BlendMode.DstIn,
            )
        }
    }

private fun DrawScope.drawCarve(top: Float, band: Float, cut: Color) {
    val mid = top + band / 2
    val w = size.width
    // Quadratic lenses from the band's centre line: control points at the band's top and bottom edges.
    drawPath(Path().apply { moveTo(0f, mid); quadraticTo(w / 2, top, w, mid); close() }, cut)
    drawPath(Path().apply { moveTo(0f, mid); quadraticTo(w / 2, top + band, w, mid); close() }, Tokens.Colors.lineCarvedLight)
}
