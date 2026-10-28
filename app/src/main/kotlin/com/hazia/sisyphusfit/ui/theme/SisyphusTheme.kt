package com.hazia.sisyphusfit.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

/** SPEC 0.34: balanced by default, compact as the person's option. */
enum class DensityMode { Balanced, Compact }

private class ThemeValues(val mode: DensityMode, val space: Tokens.Space, val size: Tokens.Size, val type: AppType)

private fun valuesFor(mode: DensityMode) = when (mode) {
    DensityMode.Balanced -> ThemeValues(mode, Tokens.Space.Balanced, Tokens.Size.Balanced, AppType(Tokens.Type.Balanced))
    DensityMode.Compact -> ThemeValues(mode, Tokens.Space.Compact, Tokens.Size.Compact, AppType(Tokens.Type.Compact))
}

private val LocalTheme = staticCompositionLocalOf { valuesFor(DensityMode.Balanced) }

/** Provides the tokens of one density. Colours, radii, strokes, elevation and motion are the same at both: read them from [Tokens]. */
@Composable
fun SisyphusTheme(density: DensityMode = DensityMode.Balanced, content: @Composable () -> Unit) {
    val values = remember(density) { valuesFor(density) }
    CompositionLocalProvider(LocalTheme provides values, content = content)
}

/** The density-dependent tokens, as provided by the nearest [SisyphusTheme]. */
object Theme {
    val density: DensityMode @Composable @ReadOnlyComposable get() = LocalTheme.current.mode
    val space: Tokens.Space @Composable @ReadOnlyComposable get() = LocalTheme.current.space
    val size: Tokens.Size @Composable @ReadOnlyComposable get() = LocalTheme.current.size
    val type: AppType @Composable @ReadOnlyComposable get() = LocalTheme.current.type
}
