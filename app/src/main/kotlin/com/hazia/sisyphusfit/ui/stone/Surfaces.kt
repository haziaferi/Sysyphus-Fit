package com.hazia.sisyphusfit.ui.stone

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.hazia.sisyphusfit.ui.theme.Theme
import com.hazia.sisyphusfit.ui.theme.Tokens

// Gap and end padding of cards and rows in the decided mockups (".mcard": 12 8 12 pad; ".row1": 0 12 0 pad; gap 12).
private val GAP = 12.dp
private val END = 8.dp

/**
 * A worn surface (SPEC:R65): module cards, week sections, the resume card ([minHeight] `size.card`) and rows
 * (`size.row`). Cut on the left, worn on the right, lit toward the worn side; padded `space.pad` at the start and
 * `space.pad-y` above and below (SPEC:R126). One you press ([pressable], or with [onClick]) stands on the light pebbles'
 * 1 dp step shadow, its edge on the page (SPEC:R126), and shows its press and focus in its shape (SPEC:R127).
 */
@Composable
fun WornCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    minHeight: Dp = Theme.size.card,
    pressable: Boolean = onClick != null,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier
            .testTag("piece:worn-card")
            .fillMaxWidth()
            .heightIn(min = minHeight)
            .then(if (pressable) Modifier.elevation(Tokens.Elevation.step, WornShape) else Modifier)
            .worn()
            .then(
                if (onClick != null) Modifier.clickable(interactionSource = stoneSource(), indication = StoneIndication(WornShape), onClick = onClick)
                else Modifier,
            )
            .padding(start = Theme.space.pad, top = Theme.space.padY, end = END, bottom = Theme.space.padY),
        horizontalArrangement = Arrangement.spacedBy(GAP),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

/** A flat row (SPEC:R65): no fill, the worn outline drawn 1 dp in `line.edge`; at least `size.row` high. */
@Composable
fun FlatRow(modifier: Modifier = Modifier, onClick: (() -> Unit)? = null, content: @Composable RowScope.() -> Unit) {
    Row(
        modifier
            .testTag("piece:flat-row")
            .fillMaxWidth()
            .heightIn(min = Theme.size.row)
            .elevation(Tokens.Elevation.edgeInset, WornShape)
            .then(
                if (onClick != null) Modifier.clickable(interactionSource = stoneSource(), indication = StoneIndication(WornShape), onClick = onClick)
                else Modifier,
            )
            .padding(start = Theme.space.pad, end = GAP),
        horizontalArrangement = Arrangement.spacedBy(GAP),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}
