package com.hazia.sisyphusfit.ui.stone

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hazia.sisyphusfit.R
import com.hazia.sisyphusfit.ui.theme.StoneText
import com.hazia.sisyphusfit.ui.theme.Theme
import com.hazia.sisyphusfit.ui.theme.Tokens
import com.hazia.sisyphusfit.ui.theme.sisifo

// Sizes of the decided mockups' controls (".box" 22, ".sw" 44 x 24 with an 18 knob inset 3); segments take size.hit.
private val BOX = 22.dp
private val TRACK_W = 44.dp
private val TRACK_H = 24.dp
private val KNOB = 18.dp
private val KNOB_INSET = 3.dp

/**
 * A checkbox: a 2 dp `line.edge` box with `radius.box`; checked, filled `fill.dark` with a white check. [label] names it.
 * Its press and focus are drawn on the box (SPEC:R127).
 */
@Composable
fun CheckBox(checked: Boolean, onChange: (Boolean) -> Unit, label: String, modifier: Modifier = Modifier) {
    val source = remember { MutableInteractionSource() }
    Box(
        modifier.minHit().semantics { contentDescription = label }
            .toggleable(checked, interactionSource = source, indication = null, role = Role.Checkbox, onValueChange = onChange),
        contentAlignment = Alignment.Center,
    ) { CheckMark(checked, Modifier.stoneIndication(source, RoundedCornerShape(Tokens.Radius.box))) }
}

/** The checkbox's box alone, for a surface that is itself the toggle (a module card): no target of its own. */
@Composable
fun CheckMark(checked: Boolean, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(Tokens.Radius.box)
    Box(
        modifier
            .testTag("piece:box")
            .size(BOX)
            .then(
                if (checked) Modifier.background(Tokens.Colors.fillDark, shape)
                else Modifier.border(Tokens.Stroke.selected, Tokens.Colors.lineEdge, shape),
            ),
        contentAlignment = Alignment.Center,
    ) { if (checked) StoneIcon(R.drawable.ic_check, tint = Tokens.Colors.fillOnDark, size = 16.dp) }
}

/** A switch: the track in `line.edge`, `fill.dark` when on; the knob in `surface.card`. [label] names it. */
@Composable
fun Switch(checked: Boolean, onChange: (Boolean) -> Unit, label: String, modifier: Modifier = Modifier) {
    val source = remember { MutableInteractionSource() }
    Box(
        modifier.minHit().semantics { contentDescription = label }
            .toggleable(checked, interactionSource = source, indication = null, role = Role.Switch, onValueChange = onChange),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            Modifier
                .testTag("piece:switch")
                .size(TRACK_W, TRACK_H)
                .background(if (checked) Tokens.Colors.fillDark else Tokens.Colors.lineEdge, CircleShape)
                .stoneIndication(source, CircleShape),
        ) {
            Box(
                Modifier
                    .offset(x = if (checked) TRACK_W - KNOB - KNOB_INSET else KNOB_INSET, y = KNOB_INSET)
                    .size(KNOB)
                    .background(Tokens.Colors.surfaceCard, CircleShape),
            )
        }
    }
}

/**
 * Segmented choices: round-ended, a hairline `line.edge`; the selected one in `surface.card` with a 2 dp `ink.primary`
 * outline and its label at 500, never the main bar's fill (SPEC:R126). At least `size.hit` high (SPEC:R50 step 1).
 */
@Composable
fun Segmented(options: List<String>, selected: Int, onSelect: (Int) -> Unit, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(Tokens.Radius.full)
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEachIndexed { i, label ->
            val on = i == selected
            val body = Theme.type.body
            Box(
                Modifier
                    .testTag("piece:segment")
                    .weight(1f)
                    .heightIn(min = Theme.size.hit)
                    .then(
                        if (on) Modifier.background(Tokens.Colors.surfaceCard, shape).border(Tokens.Stroke.selected, Tokens.Colors.inkPrimary, shape)
                        else Modifier.border(Tokens.Stroke.hair, Tokens.Colors.lineEdge, shape),
                    )
                    .selectable(on, interactionSource = stoneSource(), indication = StoneIndication(shape), role = Role.RadioButton, onClick = { onSelect(i) })
                    .padding(horizontal = 8.dp),
                contentAlignment = Alignment.Center,
            ) {
                StoneText(label, style = if (on) body.copy(fontWeight = FontWeight(500), fontFamily = sisifo(500, Theme.type.roles.body.carve)) else body)
            }
        }
    }
}
