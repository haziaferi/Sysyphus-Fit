package com.hazia.sisyphusfit.ui.stone

import androidx.compose.foundation.IndicationNodeFactory
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.node.CompositionLocalConsumerModifierNode
import androidx.compose.ui.node.DelegatableNode
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.currentValueOf
import androidx.compose.ui.node.invalidateDraw
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.hazia.sisyphusfit.ui.theme.Theme
import com.hazia.sisyphusfit.ui.theme.Tokens
import kotlinx.coroutines.launch

/** At least the density's touch target (`size.hit`, SPEC:R50: never under 42 dp) in both directions. */
@Composable
fun Modifier.minHit(): Modifier = sizeIn(minWidth = Theme.size.hit, minHeight = Theme.size.hit)

/** How a piece shows it is pressed (SPEC:R127). */
enum class PressLook {
    /** `state.pressed` drawn over the piece, in its shape: light pieces and pieces with no fill. */
    Light,

    /** The fill becomes `state.pressed-dark` under the label: the main bar. */
    Dark,
}

/** A state drawn whether or not it happens, so the gallery and its goldens can show it (SPEC:R127). */
enum class ShownState { None, Pressed, Focused }

/** The gallery's override; the app leaves it at [ShownState.None]. */
val LocalShownState = staticCompositionLocalOf { ShownState.None }

/**
 * The stone pieces' press and focus (SPEC:R127): pressed, [look] in [shape]; focused by keyboard or switch access, a
 * `stroke.focus` ring in `state.focus` following [shape] `stroke.focus-gap` outside it, on the ground around the piece.
 * Foundation's rectangular default never shows.
 */
data class StoneIndication(val shape: Shape, val look: PressLook = PressLook.Light) : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode = StoneIndicationNode(interactionSource, shape, look)
}

private class StoneIndicationNode(
    private val source: InteractionSource,
    private val shape: Shape,
    private val look: PressLook,
) : Modifier.Node(), DrawModifierNode, CompositionLocalConsumerModifierNode {
    private val presses = mutableListOf<PressInteraction.Press>()
    private val focuses = mutableListOf<FocusInteraction.Focus>()

    override fun onAttach() {
        coroutineScope.launch {
            source.interactions.collect {
                when (it) {
                    is PressInteraction.Press -> presses += it
                    is PressInteraction.Release -> presses -= it.press
                    is PressInteraction.Cancel -> presses -= it.press
                    is FocusInteraction.Focus -> focuses += it
                    is FocusInteraction.Unfocus -> focuses -= it.focus
                }
                invalidateDraw()
            }
        }
    }

    // A Release or Unfocus sent while detached is never seen, so a reused node would come back drawn pressed or focused.
    override fun onDetach() {
        presses.clear()
        focuses.clear()
    }

    override fun ContentDrawScope.draw() {
        val shown = currentValueOf(LocalShownState)
        val pressed = presses.isNotEmpty() || shown == ShownState.Pressed
        val focused = focuses.isNotEmpty() || shown == ShownState.Focused
        if (pressed && look == PressLook.Dark) drawOutline(shape.createOutline(size, layoutDirection, this), Tokens.Colors.statePressedDark)
        drawContent()
        if (pressed && look == PressLook.Light) drawOutline(shape.createOutline(size, layoutDirection, this), Tokens.Colors.statePressed)
        if (focused) {
            val width = Tokens.Stroke.focus.toPx()
            // The ring's centre line lies the gap plus half its width outside the shape.
            val out = Tokens.Stroke.focusGap.toPx() + width / 2
            translate(-out, -out) {
                val ring = shape.createOutline(Size(size.width + 2 * out, size.height + 2 * out), layoutDirection, this)
                drawOutline(ring, Tokens.Colors.stateFocus, style = Stroke(width))
            }
        }
    }
}

/**
 * A pressable area of at least `size.hit`. With [shape] it shows its own press and focus (SPEC:R127); a piece drawn
 * smaller than its touch area passes [source] instead, and draws them on its drawn shape with [Modifier.stoneIndication].
 * [description] names a control that shows no text (an icon), for TalkBack; [label] only describes the action.
 */
@Composable
fun Modifier.press(
    onClick: () -> Unit,
    label: String? = null,
    role: Role = Role.Button,
    description: String? = null,
    shape: Shape? = null,
    look: PressLook = PressLook.Light,
    source: MutableInteractionSource? = null,
): Modifier =
    minHit()
        .then(if (description != null) Modifier.semantics { contentDescription = description } else Modifier)
        .clickable(
            interactionSource = source ?: stoneSource(),
            indication = shape?.let { StoneIndication(it, look) },
            onClickLabel = label,
            role = role,
            onClick = onClick,
        )

/**
 * A remembered interaction source for a stone piece. Without one, Foundation creates the indication only at the first
 * press or focus, so a state the gallery shows ([LocalShownState]) would never be drawn.
 */
@Composable
fun stoneSource(): MutableInteractionSource = remember { MutableInteractionSource() }

/** Draws the press and focus of the control [source] belongs to on this drawn shape (SPEC:R127). */
fun Modifier.stoneIndication(source: InteractionSource, shape: Shape, look: PressLook = PressLook.Light): Modifier =
    indication(source, StoneIndication(shape, look))
