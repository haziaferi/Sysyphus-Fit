package com.hazia.sisyphusfit.ui.stone

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hazia.sisyphusfit.R
import com.hazia.sisyphusfit.ui.theme.StoneText
import com.hazia.sisyphusfit.ui.theme.Theme
import com.hazia.sisyphusfit.ui.theme.Tokens
import com.hazia.sisyphusfit.ui.theme.sisifo

// The header row of the decided mockups ("header.top": 64px high, gap 8, padding 0 4 0 gutter; 4 at left with Back).
private val HEADER_HEIGHT = 64.dp

/**
 * A session or exercise screen's header (SPEC 0.43, 0.49): Back, the title, the session clock and Finish. The title may
 * end in "…" and shows in full on a tap; the clock and Finish never shorten.
 */
@Composable
fun HeaderRow(title: String, clock: String?, onBack: (() -> Unit)?, onFinish: (() -> Unit)?, modifier: Modifier = Modifier) {
    var full by remember(title) { mutableStateOf(false) }
    Row(
        modifier
            .testTag("header")
            .fillMaxWidth()
            .heightIn(min = HEADER_HEIGHT)
            .padding(start = if (onBack != null) 4.dp else Theme.space.gutter, end = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (onBack != null) {
            Box(Modifier.press(onBack, description = "Back"), contentAlignment = Alignment.Center) { StoneIcon(R.drawable.ic_back) }
        }
        Box(
            Modifier.weight(1f).heightIn(min = Theme.size.hit).clickable(onClickLabel = "show the full title") { full = !full },
            contentAlignment = Alignment.CenterStart,
        ) {
            StoneText(
                title,
                Modifier.testTag("header-title"),
                style = Theme.type.body.copy(fontWeight = FontWeight(500), fontFamily = sisifo(500, Theme.type.roles.body.carve)),
                maxLines = if (full) Int.MAX_VALUE else 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        if (clock != null) {
            Row(Modifier.padding(horizontal = 4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp), verticalAlignment = Alignment.CenterVertically) {
                StoneIcon(R.drawable.ic_clock, tint = Tokens.Colors.inkSecondary, size = 16.dp)
                StoneText(clock, style = Theme.type.label.copy(color = Tokens.Colors.inkSecondary, fontFeatureSettings = "tnum"), maxLines = 1, softWrap = false)
            }
        }
        if (onFinish != null) Pill("Finish", onFinish, quiet = true)
    }
}
