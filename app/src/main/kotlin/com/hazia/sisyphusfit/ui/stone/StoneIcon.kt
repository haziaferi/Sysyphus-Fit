package com.hazia.sisyphusfit.ui.stone

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.hazia.sisyphusfit.ui.theme.Tokens

// The lit edge's drop, as drawn in the decided mockups ("m-icons-lit": drop-shadow 0 .7px 0 in carved-light).
private val LIT_DROP = 0.7.dp

/**
 * One of the app's 28 icons (SPEC 0.37), 24 dp, cut into the surface: [tint] over a line of `line.carved-light`
 * 0.7 dp lower (0.53). Decorative unless [contentDescription] is given by the caller's semantics.
 */
@Composable
fun StoneIcon(@DrawableRes icon: Int, modifier: Modifier = Modifier, tint: Color = Tokens.Colors.inkPrimary) {
    val painter = painterResource(icon)
    Layout(
        modifier = modifier
            .size(24.dp)
            .drawBehind {
                translate(top = LIT_DROP.toPx()) {
                    with(painter) { draw(size, colorFilter = ColorFilter.tint(Tokens.Colors.lineCarvedLight)) }
                }
                with(painter) { draw(size, colorFilter = ColorFilter.tint(tint)) }
            },
    ) { _, c -> layout(c.minWidth, c.minHeight) {} }
}
