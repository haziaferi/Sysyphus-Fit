package com.hazia.sisyphusfit.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.hazia.sisyphusfit.ui.stone.BottomZone
import com.hazia.sisyphusfit.ui.stone.MainBar
import com.hazia.sisyphusfit.ui.theme.StoneText
import com.hazia.sisyphusfit.ui.theme.Theme
import com.hazia.sisyphusfit.ui.theme.Tokens

/**
 * When the data cannot be opened (owner's call, 0.60): said plainly, on any launch, with one way on. A damaged file is
 * set aside by the database open, never removed, so "nothing has been deleted" is true.
 */
@Composable
fun CantOpenScreen(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize()) {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(Theme.space.gutter)) {
            StoneText("Your data couldn't be opened", style = Theme.type.heading)
            Spacer(Modifier.height(Theme.space.gap))
            StoneText(
                "Nothing has been deleted. Try again, and if it keeps happening, restart your phone.",
                style = Theme.type.body.copy(color = Tokens.Colors.inkSecondary),
            )
        }
        BottomZone { MainBar("Try again", onClick = onRetry) }
    }
}
