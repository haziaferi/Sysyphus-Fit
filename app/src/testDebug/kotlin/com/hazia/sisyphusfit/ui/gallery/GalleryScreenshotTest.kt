package com.hazia.sisyphusfit.ui.gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.captureRoboImage
import com.hazia.sisyphusfit.ui.stone.BottomZone
import com.hazia.sisyphusfit.ui.stone.MainBar
import com.hazia.sisyphusfit.ui.stone.TabBar
import com.hazia.sisyphusfit.ui.theme.DensityMode
import com.hazia.sisyphusfit.ui.theme.SisyphusTheme
import com.hazia.sisyphusfit.ui.theme.Tokens
import java.io.File
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

// Rasterising may differ by a few edge pixels between machines; anything over 0.02% of the image is a change
// (moving the pill by 2 dp changes 0.13%).
private val options = RoborazziOptions(compareOptions = RoborazziOptions.CompareOptions(changeThreshold = 0.0002f))

/**
 * Goldens of every piece (balanced and compact at font size 1.0, 1.3 and 2.0, tall enough to hold them all) and of the
 * gallery in the 6T's app area (384 x 756 dp at 450 dpi, SPEC 0.48), closed and with the sheet open.
 * `./gradlew :app:testDebugUnitTest -Proborazzi.record=true` records them; a plain run verifies against them.
 */
@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class GalleryScreenshotTest {
    @get:Rule val rule = createComposeRule()

    private fun pieces(density: DensityMode, scale: Float) {
        RuntimeEnvironment.setFontScale(scale)
        rule.setContent {
            SisyphusTheme(density) {
                Column(Modifier.background(Tokens.Colors.surfacePage)) {
                    PieceSamples()
                    BottomZone { MainBar("Done · set 2", onClick = {}) }
                    TabBar(GalleryTabs, 0, {})
                }
            }
        }
        rule.onRoot().captureRoboImage("pieces-${density.name.lowercase()}-${(scale * 100).toInt()}.png", roborazziOptions = options)
    }

    @Test @Config(sdk = [36], qualifiers = "w384dp-h2400dp-xhdpi") fun balanced100() = pieces(DensityMode.Balanced, 1f)
    @Test @Config(sdk = [36], qualifiers = "w384dp-h2400dp-xhdpi") fun balanced130() = pieces(DensityMode.Balanced, 1.3f)
    @Test @Config(sdk = [36], qualifiers = "w384dp-h2400dp-xhdpi") fun balanced200() = pieces(DensityMode.Balanced, 2f)
    @Test @Config(sdk = [36], qualifiers = "w384dp-h2400dp-xhdpi") fun compact100() = pieces(DensityMode.Compact, 1f)
    @Test @Config(sdk = [36], qualifiers = "w384dp-h2400dp-xhdpi") fun compact130() = pieces(DensityMode.Compact, 1.3f)
    @Test @Config(sdk = [36], qualifiers = "w384dp-h2400dp-xhdpi") fun compact200() = pieces(DensityMode.Compact, 2f)

    @Test @Config(sdk = [36], qualifiers = "w384dp-h756dp-450dpi")
    fun phone() {
        rule.setContent { Gallery() }
        rule.onRoot().captureRoboImage("gallery-6t.png", roborazziOptions = options)
    }

    @Test @Config(sdk = [36], qualifiers = "w384dp-h756dp-450dpi")
    fun phoneSheet() {
        rule.mainClock.autoAdvance = true
        rule.setContent { Gallery(startSheet = true) }
        rule.waitForIdle()
        rule.onRoot().captureRoboImage("gallery-6t-sheet.png", roborazziOptions = options)
    }

    /** Each piece's visual bounds (dp) at balanced 1.0, for compose_check.py to compare with the mockups. */
    @Test @Config(sdk = [36], qualifiers = "w384dp-h2400dp-xhdpi")
    fun exportBounds() = export(sheet = false, name = "bounds")

    /** The sheet open, for its grab handle (the scrim would grey every other piece). */
    @Test @Config(sdk = [36], qualifiers = "w384dp-h2400dp-xhdpi")
    fun exportSheetBounds() = export(sheet = true, name = "bounds-sheet")

    private fun export(sheet: Boolean, name: String) {
        rule.setContent { Gallery(startSheet = sheet) }
        rule.waitForIdle()
        val d = rule.density.density
        val rows = rule.onRoot(useUnmergedTree = true).fetchSemanticsNode().let { root ->
            buildList {
                fun walk(n: androidx.compose.ui.semantics.SemanticsNode) {
                    val tag = n.config.getOrNull(SemanticsProperties.TestTag)
                    if (tag != null && (tag.startsWith("piece:") || tag == "header" || tag == "tabbar")) {
                        val b = n.boundsInRoot
                        add("""{"tag":"$tag","x":${b.left / d},"y":${b.top / d},"w":${b.width / d},"h":${b.height / d}}""")
                    }
                    n.children.forEach(::walk)
                }
                walk(root)
            }
        }
        File("build/compose-check").mkdirs()
        File("build/compose-check/$name.json").writeText("[\n" + rows.joinToString(",\n") + "\n]\n")
        // The same composition as a picture (not a golden), for compose_check.py's side-by-side crops.
        File("build/compose-check/$name.png").outputStream().use {
            rule.onRoot().captureToImage().asAndroidBitmap().compress(android.graphics.Bitmap.CompressFormat.PNG, 100, it)
        }
    }
}
