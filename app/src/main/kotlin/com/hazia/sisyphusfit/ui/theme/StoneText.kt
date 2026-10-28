package com.hazia.sisyphusfit.ui.theme

import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import kotlin.math.roundToInt

private class LineCount { var lines = 1 }

/**
 * Text in a CSS line box: each line exactly the style's line height, the glyphs centred in it. Compose keeps a font's
 * full height when it is taller than the line (Sisifo's 40 sp set number in a 44 sp line measures 54); the decided
 * mockups were drawn and judged with CSS line boxes, where the glyphs overflow instead.
 */
@Composable
fun StoneText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    softWrap: Boolean = true,
) {
    val count = remember { LineCount() }
    BasicText(
        text,
        modifier.layout { measurable, constraints ->
            val p = measurable.measure(constraints.copy(minHeight = 0, maxHeight = Int.MAX_VALUE))
            // onTextLayout ran during measure(), so the line count belongs to this layout.
            val box = if (style.lineHeight.isSp) (count.lines * style.lineHeight.toPx()).roundToInt() else p.height
            val h = box.coerceIn(constraints.minHeight, constraints.maxHeight)
            layout(p.width, h) { p.place(0, (h - p.height) / 2) }
        },
        style = style,
        onTextLayout = { r: TextLayoutResult -> count.lines = r.lineCount },
        overflow = overflow,
        softWrap = softWrap,
        maxLines = maxLines,
    )
}
