package com.hazia.sisyphusfit.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * After a damaged file was set aside and a fresh database opened (SPEC:R35, SPEC:R122): said before anything else, on every
 * launch until "Start fresh", as a one-message screen (SPEC:R128). Restoring a backup joins it with the backup package.
 */
@Composable
fun SetAsideScreen(onContinue: () -> Unit, modifier: Modifier = Modifier) {
    MessageScreen(
        title = "Your data was damaged",
        line = "It couldn't be read, so it was set aside on your phone, not deleted. Your old data is kept and can be " +
            "restored once backups arrive in a later update. Until then the app starts again from the beginning.",
        action = "Start fresh",
        onAction = onContinue,
        modifier = modifier,
    )
}
