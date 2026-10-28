package com.hazia.sisyphusfit.ui.gallery

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.hazia.sisyphusfit.R
import com.hazia.sisyphusfit.ui.stone.BottomSheet
import com.hazia.sisyphusfit.ui.stone.BottomZone
import com.hazia.sisyphusfit.ui.stone.Eyebrow
import com.hazia.sisyphusfit.ui.stone.MainBar
import com.hazia.sisyphusfit.ui.stone.Pill
import com.hazia.sisyphusfit.ui.stone.Segmented
import com.hazia.sisyphusfit.ui.stone.TabBar
import com.hazia.sisyphusfit.ui.stone.TabItem
import com.hazia.sisyphusfit.ui.theme.DensityMode
import com.hazia.sisyphusfit.ui.theme.SisyphusTheme
import com.hazia.sisyphusfit.ui.theme.StoneText
import com.hazia.sisyphusfit.ui.theme.Theme
import com.hazia.sisyphusfit.ui.theme.Tokens
import com.hazia.sisyphusfit.ui.theme.stoneEdgeToEdge

/** Debug only: every shared piece on one screen, to judge on the phone at true size (SPEC 0.48). */
class GalleryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        stoneEdgeToEdge()
        setContent { Gallery() }
    }
}

val GalleryTabs = listOf(
    TabItem("Start", R.drawable.ic_tab_start), TabItem("This week", R.drawable.ic_tab_week),
    TabItem("History", R.drawable.ic_tab_history), TabItem("More", R.drawable.ic_tab_more),
)

private val FONT_SCALES = listOf(1f, 1.3f, 2f)

/**
 * The gallery: density and font size switches over the pieces, a main bar in its bottom zone and the tab bar.
 * [fontScale] null follows the phone's own font size; a value overrides it on Android's own curve.
 */
@Composable
fun Gallery(startDensity: DensityMode = DensityMode.Balanced, startSheet: Boolean = false) {
    var density by remember { mutableStateOf(startDensity) }
    var scale by remember { mutableIntStateOf(-1) }
    var tab by remember { mutableIntStateOf(0) }
    var sheet by remember { mutableStateOf(startSheet) }
    val context = LocalContext.current
    val config = LocalConfiguration.current
    // Density(context) applies Android 14's nonlinear font curve, as the phone's own setting does.
    val scaled = if (scale < 0) LocalDensity.current else remember(scale, config) {
        Density(context.createConfigurationContext(android.content.res.Configuration(config).apply { fontScale = FONT_SCALES[scale] }))
    }
    CompositionLocalProvider(LocalDensity provides scaled) {
        SisyphusTheme(density) {
            Box(Modifier.fillMaxSize().background(Tokens.Colors.surfacePage)) {
                Column(Modifier.fillMaxSize()) {
                    Column(Modifier.weight(1f).windowInsetsPadding(WindowInsets.statusBars).verticalScroll(rememberScrollState())) {
                        Column(Modifier.padding(Theme.space.gutter)) {
                            StoneText("Gallery · density and font size", style = Theme.type.eyebrow.copy(color = Tokens.Colors.inkSecondary))
                            Segmented(listOf("Balanced", "Compact"), density.ordinal, { density = DensityMode.entries[it] })
                            Segmented(listOf("Phone", "1.0", "1.3", "2.0"), scale + 1, { scale = it - 1 })
                        }
                        PieceSamples()
                        Pill("Open a note", onClick = { sheet = true }, icon = R.drawable.ic_info)
                    }
                    // The tab bar below takes the navigation bar's inset; a doing screen's bottom zone, lowest on its
                    // screen, takes it itself.
                    BottomZone(Modifier.consumeWindowInsets(WindowInsets.navigationBars)) {
                        MainBar("Done · set 2", onClick = {}, icon = R.drawable.ic_check)
                    }
                    TabBar(GalleryTabs, tab, { tab = it })
                }
                BottomSheet(sheet, onDismiss = { sheet = false }) {
                    Eyebrow("Evidence note")
                    StoneText(
                        "At equal weekly sets, 25 studies in adults, including trained people, found no difference in muscle " +
                            "growth between training more or less often.",
                        style = Theme.type.rowTitle,
                    )
                    BottomZone { MainBar("Close", onClick = { sheet = false }) }
                }
            }
        }
    }
}
