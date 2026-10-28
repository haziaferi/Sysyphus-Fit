package com.hazia.sisyphusfit.ui.stone

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.hazia.sisyphusfit.R
import com.hazia.sisyphusfit.ui.theme.StoneText
import com.hazia.sisyphusfit.ui.theme.Theme
import com.hazia.sisyphusfit.ui.theme.Tokens

// The chip as decided in 0.47 (".cmp .chipv": 28 high, padding 0 10 0 7, gap 6, a 13 dp icon); it grows with its text (0.49).
private val CHIP_HEIGHT = 28.dp
private val CHIP_ICON = 13.dp

/** The evidence-note chip (SPEC 0.16, 0.47): the circled "i" and the note's title between two carved lines. */
@Composable
fun Chip(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier.press(onClick, label = "open the note"), contentAlignment = Alignment.Center) {
        Row(
            Modifier
                .testTag("piece:chip")
                .heightIn(min = CHIP_HEIGHT)
                .carvedLine(CarvedEdge.Top)
                .carvedLine(CarvedEdge.Bottom)
                .padding(start = 7.dp, end = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            StoneIcon(R.drawable.ic_info, tint = Tokens.Colors.inkSecondary, size = CHIP_ICON)
            StoneText(text, style = Theme.type.chip)
        }
    }
}

/** Tags as text (SPEC 0.53, "q-tag-text"): in `ink.secondary`, joined by a middle dot. */
@Composable
fun Tags(tags: List<String>, modifier: Modifier = Modifier) {
    StoneText(tags.joinToString(" · "), modifier, style = Theme.type.label.copy(color = Tokens.Colors.inkSecondary))
}

/** A section head (§10 type, eyebrow role): capitals, tracked, in `ink.secondary`. */
@Composable
fun Eyebrow(text: String, modifier: Modifier = Modifier) {
    val role = Theme.type.roles.eyebrow
    StoneText(if (role.upper) text.uppercase() else text, modifier, style = Theme.type.eyebrow.copy(color = Tokens.Colors.inkSecondary))
}
