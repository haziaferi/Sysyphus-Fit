package com.hazia.sisyphusfit.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.em
import com.hazia.sisyphusfit.R

/** Sisifo at one instance of its two axes (SPEC 0.40): weight 300-700, CARV 0-100. */
fun sisifo(weight: Int, carve: Float, italic: Boolean = false): FontFamily = FontFamily(
    Font(
        if (italic) R.font.sisifo_italic else R.font.sisifo,
        weight = FontWeight(weight),
        style = if (italic) FontStyle.Italic else FontStyle.Normal,
        variationSettings = FontVariation.Settings(FontVariation.weight(weight), FontVariation.Setting("CARV", carve)),
    ),
)

// Line heights are exact, as in the mockups: no trimming, the text centred in its line.
private val exactLine = LineHeightStyle(LineHeightStyle.Alignment.Center, LineHeightStyle.Trim.None)

fun TypeRole.toTextStyle(): TextStyle = TextStyle(
    color = Tokens.Colors.inkPrimary,
    fontFamily = sisifo(weight, carve),
    fontWeight = FontWeight(weight),
    fontSize = size,
    lineHeight = line,
    letterSpacing = tracking.em,
    fontFeatureSettings = features.ifEmpty { null },
    lineHeightStyle = exactLine,
)

/** The type roles as text styles for one density. A role with [TypeRole.upper] is set in capitals by its piece. */
class AppType(val roles: Tokens.Type) {
    val label = roles.label.toTextStyle()
    val eyebrow = roles.eyebrow.toTextStyle()
    val body = roles.body.toTextStyle()
    val rowTitle = roles.rowTitle.toTextStyle()
    val heading = roles.heading.toTextStyle()
    val exercise = roles.exercise.toTextStyle()
    val setNumber = roles.setNumber.toTextStyle()
    val comparisonLabel = roles.comparisonLabel.toTextStyle()
    val comparisonEyebrow = roles.comparisonEyebrow.toTextStyle()
    val comparisonBody = roles.comparisonBody.toTextStyle()
    val chip = roles.chip.toTextStyle().copy(color = Tokens.Colors.inkSecondary)
}
