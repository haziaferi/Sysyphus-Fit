package com.hazia.sisyphusfit.ui.stone

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hazia.sisyphusfit.ui.theme.StoneText
import com.hazia.sisyphusfit.ui.theme.Theme
import com.hazia.sisyphusfit.ui.theme.Tokens
import com.hazia.sisyphusfit.ui.theme.sisifo

// The pill's height in the decided mockups (".pill": 36px); it grows with its label (0.49).
private val PILL_HEIGHT = 36.dp

/**
 * A secondary button (SPEC 0.53): a worn pebble in `surface.sunk` standing on the 2 dp ground shadow. [quiet] drops the
 * pebble and keeps the label (the header's Finish). The touch area is at least `size.hit`.
 */
@Composable
fun Pill(label: String, onClick: () -> Unit, modifier: Modifier = Modifier, @DrawableRes icon: Int? = null, quiet: Boolean = false) {
    val shape = remember { PebbleShape(Tokens.Radius.pebblePill) }
    val style = Theme.type.body.copy(fontWeight = FontWeight(500), fontFamily = sisifo(500, Theme.type.roles.body.carve))
    Box(modifier.press(onClick), contentAlignment = Alignment.Center) {
        Row(
            Modifier
                .testTag(if (quiet) "piece:pill-quiet" else "piece:pill")
                .heightIn(min = PILL_HEIGHT)
                .then(if (quiet) Modifier else Modifier.elevation(Tokens.Elevation.ground, shape).background(Tokens.Colors.surfaceSunk, shape))
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (icon != null) StoneIcon(icon)
            StoneText(label, style = style)
        }
    }
}
