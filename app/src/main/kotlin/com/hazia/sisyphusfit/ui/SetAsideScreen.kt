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
 * After a damaged file was set aside and a fresh database opened (SPEC:R35, SPEC:R122): said before anything else, on every
 * launch until "Continue". Restoring a backup joins it with the backup package.
 */
@Composable
fun SetAsideScreen(onContinue: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize()) {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(Theme.space.gutter)) {
            StoneText("Your data was damaged", style = Theme.type.heading)
            Spacer(Modifier.height(Theme.space.gap))
            StoneText(
                "It couldn't be read, so it was set aside on your phone, not deleted. The app starts again from the " +
                    "beginning; restoring a backup comes in a later update.",
                style = Theme.type.body.copy(color = Tokens.Colors.inkSecondary),
            )
        }
        BottomZone { MainBar("Continue", onClick = onContinue) }
    }
}
