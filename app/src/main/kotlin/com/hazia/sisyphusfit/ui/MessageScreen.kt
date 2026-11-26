package com.hazia.sisyphusfit.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.hazia.sisyphusfit.ui.stone.BottomZone
import com.hazia.sisyphusfit.ui.stone.MainBar
import com.hazia.sisyphusfit.ui.theme.StoneText
import com.hazia.sisyphusfit.ui.theme.Theme
import com.hazia.sisyphusfit.ui.theme.Tokens

/**
 * A screen that tells one thing and offers one action (SPEC:R128): its title and line sit just above the bottom zone,
 * next to the action, and the space above stays empty. At large font sizes the words scroll above the fixed bar.
 */
@Composable
fun MessageScreen(title: String, line: String, action: String, onAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize()) {
        // The weight fixes the scroll area's height, which the scroll passes on as the column's minimum: Bottom then
        // puts the words at its foot.
        Column(
            Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(Theme.space.gutter).testTag("message"),
            verticalArrangement = Arrangement.Bottom,
        ) {
            StoneText(title, style = Theme.type.heading)
            Spacer(Modifier.height(Theme.space.gap))
            StoneText(line, style = Theme.type.body.copy(color = Tokens.Colors.inkSecondary))
        }
        BottomZone { MainBar(action, onClick = onAction) }
    }
}
