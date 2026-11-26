package com.hazia.sisyphusfit.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * When the data cannot be opened, such as after a failed migration (SPEC:R92): said plainly, on any launch, with one way
 * on, as a one-message screen (SPEC:R128). The file is left as it was (SPEC:R33) or set aside (SPEC:R35), never deleted, so
 * "nothing has been deleted" is true; a file set aside is told by SetAsideScreen once an open succeeds (SPEC:R122).
 */
@Composable
fun CantOpenScreen(onRetry: () -> Unit, modifier: Modifier = Modifier) {
    MessageScreen(
        title = "Your data couldn't be opened",
        line = "Nothing has been deleted. Try again; if it keeps failing, the app may need an update.",
        action = "Try again",
        onAction = onRetry,
        modifier = modifier,
    )
}
