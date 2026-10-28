package com.hazia.sisyphusfit.ui.stone

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import com.hazia.sisyphusfit.ui.theme.ShadowSpec

/** A CSS box-shadow token as Compose draws it: Compose's radius is CSS's blur, spread and offset are the same. */
fun ShadowSpec.toShadow() = Shadow(radius = blur, color = color, spread = spread, offset = DpOffset(x, y))

/** The token's shadows, in CSS order (the first is drawn on top), outside or inside [shape]. */
fun Modifier.elevation(spec: List<ShadowSpec>, shape: Shape): Modifier =
    spec.asReversed().fold(this) { m, s -> if (s.inset) m.innerShadow(shape, s.toShadow()) else m.dropShadow(shape, s.toShadow()) }
