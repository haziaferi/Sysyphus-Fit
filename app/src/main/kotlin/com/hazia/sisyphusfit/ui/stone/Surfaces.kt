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

// Inside padding and gap of cards and rows in the decided mockups (".mcard": 12 8 12 pad; ".row1": 0 12 0 pad; gap 12).
private val GAP = 12.dp

/**
 * A worn surface (SPEC 0.53): module cards, week sections, the resume card ([minHeight] `size.card`) and rows
 * (`size.row`). Cut on the left, worn on the right, lit toward the worn side.
 */
@Composable
fun WornCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    minHeight: Dp = Theme.size.card,
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier
            .testTag("piece:worn-card")
            .fillMaxWidth()
            .heightIn(min = minHeight)
            .worn()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(start = Theme.space.pad, top = 12.dp, end = 8.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(GAP),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}

/** A flat row (SPEC 0.53): no fill, the worn outline drawn 1 dp in `line.edge`; at least `size.row` high. */
@Composable
fun FlatRow(modifier: Modifier = Modifier, onClick: (() -> Unit)? = null, content: @Composable RowScope.() -> Unit) {
    Row(
        modifier
            .testTag("piece:flat-row")
            .fillMaxWidth()
            .heightIn(min = Theme.size.row)
            .elevation(Tokens.Elevation.edgeInset, WornShape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(start = Theme.space.pad, end = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(GAP),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
    )
}
