package com.hazia.sisyphusfit.ui.stone

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.hazia.sisyphusfit.ui.theme.StoneText
import com.hazia.sisyphusfit.ui.theme.Theme
import com.hazia.sisyphusfit.ui.theme.Tokens
import com.hazia.sisyphusfit.ui.theme.sisifo

// The main bar's worn dip and its light, as drawn in the decided mockups ("sv-wear", SPEC 0.53): x on a 328-wide
// grid stretched to the bar, y in dp, so the dip stays 8 dp deep when the bar grows with its label (0.49).
private const val GRID = 328f
private val DIP = 8.dp
private val LABEL_TOP = 8.dp
private val LIGHT_TOP = 1.dp
private val LIGHT_BOTTOM = 18.dp
private const val LIGHT_ALPHA = 0.8f

/** The bar's outline: square corners, its top edge sinking [DIP] across the middle third in one soft curve. */
object DipShape : Shape {
    override fun createOutline(size: Size, layoutDirection: LayoutDirection, density: Density): Outline {
        val x = size.width / GRID
        val d = with(density) { DIP.toPx() }
        return Outline.Generic(Path().apply {
            moveTo(0f, 0f)
            lineTo(102 * x, 0f)
            cubicTo(130 * x, 0f, 138 * x, d, 164 * x, d)
            cubicTo(190 * x, d, 198 * x, 0f, 226 * x, 0f)
            lineTo(size.width, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        })
    }
}

/**
 * The main action of a doing screen (SPEC 0.35, 0.53): full width in the bottom zone, `fill.dark`, the dip lit faintly
 * by `fill.dark-light`. At least `size.button` high; its label wraps to two lines rather than shortening (0.49).
 */
@Composable
fun MainBar(label: String, onClick: () -> Unit, modifier: Modifier = Modifier, @DrawableRes icon: Int? = null) {
    val style = Theme.type.body.copy(
        color = Tokens.Colors.fillOnDark,
        fontWeight = FontWeight(500),
        fontFamily = sisifo(500, Theme.type.roles.body.carve),
        textAlign = TextAlign.Center,
    )
    Row(
        modifier
            .testTag("piece:main-bar")
            .fillMaxWidth()
            .heightIn(min = Theme.size.button)
            .background(Tokens.Colors.fillDark, DipShape)
            .drawBehind {
                val x = size.width / GRID
                val top = LIGHT_TOP.toPx()
                val bottom = LIGHT_BOTTOM.toPx()
                val d = DIP.toPx()
                val light = Path().apply {
                    moveTo(106 * x, top)
                    cubicTo(132 * x, top, 140 * x, top + d, 164 * x, top + d)
                    cubicTo(188 * x, top + d, 196 * x, top, 222 * x, top)
                    lineTo(222 * x, bottom)
                    lineTo(106 * x, bottom)
                    close()
                }
                drawPath(
                    light,
                    Brush.verticalGradient(
                        listOf(Tokens.Colors.fillDarkLight, Tokens.Colors.fillDark.copy(alpha = 0f)),
                        startY = top, endY = bottom,
                    ),
                    alpha = LIGHT_ALPHA,
                )
            }
            .press(onClick)
            .padding(start = 24.dp, end = 24.dp, top = LABEL_TOP),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (icon != null) StoneIcon(icon, tint = Tokens.Colors.fillOnDark)
        StoneText(label, Modifier.weight(1f, fill = false), style = style)
    }
}

/** The bottom zone behind the main bar (SPEC 0.35): `surface.sunk`, reaching under the navigation bar (edge to edge). */
@Composable
fun BottomZone(modifier: Modifier = Modifier, content: @Composable RowScope.() -> Unit) {
    Row(
        modifier
            .testTag("piece:bottom-zone")
            .fillMaxWidth()
            .background(Tokens.Colors.surfaceSunk)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(start = Theme.space.gutter, end = Theme.space.gutter, top = Theme.space.foot, bottom = Theme.space.foot + 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        content = content,
    )
}
