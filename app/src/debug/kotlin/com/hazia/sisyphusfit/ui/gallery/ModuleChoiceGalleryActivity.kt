package com.hazia.sisyphusfit.ui.gallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.hazia.sisyphusfit.ui.modules.IncludesMark
import com.hazia.sisyphusfit.ui.modules.ModuleChoiceScreen
import com.hazia.sisyphusfit.ui.stone.Pill
import com.hazia.sisyphusfit.ui.stone.Segmented
import com.hazia.sisyphusfit.ui.theme.DensityMode
import com.hazia.sisyphusfit.ui.theme.SisyphusTheme
import com.hazia.sisyphusfit.ui.theme.Theme
import com.hazia.sisyphusfit.ui.theme.Tokens
import com.hazia.sisyphusfit.ui.theme.stoneEdgeToEdge

/** Debug only, until the owner picks on the 6T: the module choice with its mark, icons, density and font size switchable. */
class ModuleChoiceGalleryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stoneEdgeToEdge()
        setContent { ModuleChoiceGallery() }
    }
}

@Composable
fun ModuleChoiceGallery() {
    var shown by remember { mutableStateOf(true) }
    var mark by remember { mutableIntStateOf(0) }
    var icons by remember { mutableIntStateOf(0) }
    var density by remember { mutableStateOf(DensityMode.Balanced) }
    var scale by remember { mutableIntStateOf(-1) }
    CompositionLocalProvider(LocalDensity provides rememberGalleryDensity(scale)) {
        SisyphusTheme(density) {
            Column(Modifier.fillMaxSize().background(Tokens.Colors.surfacePage).windowInsetsPadding(WindowInsets.statusBars)) {
                Column(Modifier.padding(horizontal = Theme.space.gutter), verticalArrangement = Arrangement.spacedBy(Theme.space.gap)) {
                    Pill(if (shown) "Hide variants" else "Variants", onClick = { shown = !shown }, quiet = true)
                    if (shown) {
                        Segmented(listOf("✦", "⟐", "info"), mark, { mark = it })
                        Segmented(listOf("With icons", "Without"), icons, { icons = it })
                        Segmented(listOf("Balanced", "Compact"), density.ordinal, { density = DensityMode.entries[it] })
                        Segmented(listOf("Phone", "1.0", "1.3", "2.0"), scale + 1, { scale = it - 1 })
                    }
                }
                ModuleChoiceScreen(onStepForward = {}, Modifier.weight(1f), IncludesMark.entries[mark], moduleIcons = icons == 0)
            }
        }
    }
}
