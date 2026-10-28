package com.hazia.sisyphusfit.ui.stone

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import com.hazia.sisyphusfit.R
import com.hazia.sisyphusfit.ui.theme.PebbleRadii
import com.hazia.sisyphusfit.ui.theme.StoneText
import com.hazia.sisyphusfit.ui.theme.Theme
import com.hazia.sisyphusfit.ui.theme.Tokens

// From the decided mockups (".bigrow", ".big .val"): the row's side padding, the gap beside the value, the value's
// own padding and its minimum tap size.
private val ROW_PAD = 8.dp
private val GAP = 8.dp
private val VALUE_PAD = 8.dp
private val VALUE_MIN = 48.dp

/** How the value and its unit fit between the pebbles (SPEC 0.49). */
data class StepperFit(val size: TextUnit, val unitUnder: Boolean)

/**
 * SPEC 0.49: the value keeps its role's size if it fits beside its unit; else it shrinks, never below [min]; if it
 * still does not fit, the unit moves under it and the value takes the largest size (from [full] to [min]) that fits alone.
 */
fun stepperFit(full: TextUnit, min: TextUnit, room: Dp, valueWidth: (TextUnit) -> Dp, unitWidth: Dp): StepperFit {
    val sizes = generateSequence(full.value) { it - 1f }.takeWhile { it >= min.value }.map { it.sp }.toList().ifEmpty { listOf(min) }
    sizes.firstOrNull { valueWidth(it) + 2 * (unitWidth + GAP) <= room }?.let { return StepperFit(it, false) }
    return StepperFit(sizes.firstOrNull { valueWidth(it) <= room } ?: sizes.last(), true)
}

/**
 * A set value with its − / + pebbles (SPEC 0.28, 0.53): borderless light pebbles on the 1 dp step shadow, the number you
 * tap to type underlined 1.5 dp in `line.edge`, its unit beside it. [small] is the partials row (smaller pebbles and
 * value); [pebble] is `radius.pebble-b` for weight and partials, `radius.pebble-a` for reps. [name] names the
 * pebbles for TalkBack ("Decrease kg").
 */
@Composable
fun Stepper(
    value: String,
    unit: String?,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
    onTapValue: () -> Unit,
    modifier: Modifier = Modifier,
    small: Boolean = false,
    pebble: PebbleRadii = Tokens.Radius.pebbleB,
    name: String = unit ?: "value",
) {
    val pebbleSize = if (small) Theme.size.stepperSmall else Theme.size.stepper
    val role = if (small) Theme.type.heading else Theme.type.setNumber
    val min = if (small) Tokens.Type.Compact.heading.size else Tokens.Type.Compact.setNumber.size
    val unitStyle = Theme.type.label.copy(color = Tokens.Colors.inkSecondary)
    val measurer = rememberTextMeasurer()
    val density = LocalDensity.current
    Row(modifier.fillMaxWidth().padding(horizontal = ROW_PAD), verticalAlignment = Alignment.CenterVertically) {
        StepPebble(R.drawable.ic_minus, "Decrease $name", onMinus, pebbleSize, pebble)
        BoxWithConstraints(Modifier.weight(1f), contentAlignment = Alignment.Center) {
            val fit = with(density) {
                val unitWidth = if (unit == null) 0.dp else measurer.measure(unit, unitStyle).size.width.toDp()
                stepperFit(
                    role.fontSize, min, maxWidth,
                    { s -> maxOf(VALUE_MIN, measurer.measure(value, role.copy(fontSize = s)).size.width.toDp() + 2 * VALUE_PAD) },
                    unitWidth,
                )
            }
            val style = role.copy(fontSize = fit.size, lineHeight = role.lineHeight * (fit.size.value / role.fontSize.value))
            val valueText = @Composable { TappableValue(value, style, onTapValue) }
            val unitText = @Composable { if (unit != null) StoneText(unit, Modifier.testTag("stepper-unit"), style = unitStyle) }
            if (fit.unitUnder) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) { valueText(); unitText() }
            } else {
                // The value centred, its unit to its right: the mockup's grid of 1fr | value | 1fr.
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.weight(1f))
                    valueText()
                    Box(Modifier.weight(1f).padding(start = GAP)) { unitText() }
                }
            }
        }
        StepPebble(R.drawable.ic_plus, "Increase $name", onPlus, pebbleSize, pebble)
    }
}

@Composable
private fun TappableValue(value: String, style: TextStyle, onTap: () -> Unit) {
    Box(
        Modifier.testTag("piece:value").sizeIn(minWidth = VALUE_MIN, minHeight = VALUE_MIN).press(onTap, label = "type a value").padding(horizontal = VALUE_PAD),
        contentAlignment = Alignment.Center,
    ) {
        StoneText(
            value,
            Modifier.testTag("stepper-value").drawBehind {
                val h = Tokens.Stroke.line.toPx()
                drawLine(Tokens.Colors.lineEdge, Offset(0f, size.height - h / 2), Offset(size.width, size.height - h / 2), h)
            },
            style = style,
            maxLines = 1,
        )
    }
}

@Composable
private fun StepPebble(icon: Int, label: String, onClick: () -> Unit, size: Dp, radii: PebbleRadii) {
    val shape = remember(radii) { PebbleShape(radii) }
    Box(
        Modifier
            .testTag("piece:pebble")
            .size(size)
            .elevation(Tokens.Elevation.step, shape)
            .background(Tokens.Colors.surfaceCard, shape)
            .press(onClick, description = label),
        contentAlignment = Alignment.Center,
    ) { StoneIcon(icon) }
}
