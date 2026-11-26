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
 * When the data cannot be opened with nothing set aside, such as after a failed migration (SPEC:R92): said plainly, on any
 * launch, with one way on. The file is left as it was (SPEC:R33), so "nothing has been deleted" is true; a file set aside
 * leads to SetAsideScreen instead (SPEC:R122).
 */
@Composable
fun CantOpenScreen(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize()) {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(Theme.space.gutter)) {
            StoneText("Your data couldn't be opened", style = Theme.type.heading)
            Spacer(Modifier.height(Theme.space.gap))
            StoneText(
                "Nothing has been deleted. Try again; if it keeps failing, the app may need an update.",
                style = Theme.type.body.copy(color = Tokens.Colors.inkSecondary),
            )
        }
        BottomZone { MainBar("Try again", onClick = onRetry) }
    }
}
