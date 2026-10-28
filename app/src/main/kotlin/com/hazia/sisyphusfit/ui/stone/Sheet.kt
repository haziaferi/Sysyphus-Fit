package com.hazia.sisyphusfit.ui.stone

import androidx.compose.animation.AnimatedVisibility
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.isTraversalGroup
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.hazia.sisyphusfit.ui.theme.Theme
import com.hazia.sisyphusfit.ui.theme.Tokens
import kotlin.math.roundToInt

// The sheet's grab handle in the decided mockups (".sheet .grab": 32 x 4, 4 above and 8 below; body padding 8).
private val GRAB_W = 32.dp
private val GRAB_H = 4.dp
// A drag down past a quarter of the sheet's height, or a fling faster than Android's own sheets take as one (125 dp/s),
// closes it; less springs back.
private const val DISMISS_FRACTION = 0.25f
private val FLING = 125.dp

/**
 * A bottom sheet (SPEC 0.16, evidence notes; the labels list): `surface.page` with `radius.sheet` top corners and
 * `elevation.float`, over `shadow.scrim`. Tapping the scrim, dragging or flinging the sheet down, or system Back closes
 * it; TalkBack reads it as a pane. It slides with `motion.valley`, the app's one decided slide.
 */
@Composable
fun BottomSheet(visible: Boolean, onDismiss: () -> Unit, modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    val easing = checkNotNull(Tokens.Motion.valley.easing)
    val spec = tween<Float>(Tokens.Motion.valley.durationMs, easing = easing)
    val slide = tween<IntOffset>(Tokens.Motion.valley.durationMs, easing = easing)
    BackHandler(enabled = visible, onBack = onDismiss)
    Box(modifier.fillMaxSize()) {
        AnimatedVisibility(visible, enter = fadeIn(spec), exit = fadeOut(spec)) {
            Box(
                Modifier
                    .fillMaxSize()
                    .testTag("scrim")
                    .background(Tokens.Colors.shadowScrim)
                    .clickable(interactionSource = remember { MutableInteractionSource() }, indication = null, onClickLabel = "close", onClick = onDismiss),
            )
        }
        AnimatedVisibility(
            visible,
            Modifier.align(Alignment.BottomCenter),
            enter = slideInVertically(slide) { it },
            exit = slideOutVertically(slide) { it },
        ) {
            val shape = RoundedCornerShape(topStart = Tokens.Radius.sheet, topEnd = Tokens.Radius.sheet)
            // The offset is written as the finger moves, so a spring-back can't be overtaken by a late drag step.
            var drag by remember { mutableFloatStateOf(0f) }
            val height = remember { floatArrayOf(1f) }
            val fling = with(LocalDensity.current) { FLING.toPx() }
            Column(
                Modifier
                    .fillMaxWidth()
                    .semantics {
                        paneTitle = "Sheet"
                        isTraversalGroup = true
                        dismiss { onDismiss(); true }
                    }
                    .onSizeChanged { height[0] = it.height.toFloat() }
                    // Measured before the offset, so the drag reads the finger's speed, not the sheet's own motion.
                    .draggable(
                        rememberDraggableState { dy -> drag = (drag + dy).coerceAtLeast(0f) },
                        Orientation.Vertical,
                        onDragStopped = { velocity ->
                            if (drag > height[0] * DISMISS_FRACTION || velocity > fling) {
                                onDismiss()
                            } else {
                                animate(drag, 0f, animationSpec = spec) { v, _ -> drag = v }
                            }
                        },
                    )
                    .offset { IntOffset(0, drag.roundToInt()) }
                    .elevation(Tokens.Elevation.float, shape)
                    .background(Tokens.Colors.surfacePage, shape)
                    .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Bottom + WindowInsetsSides.Horizontal))
                    .padding(horizontal = Theme.space.gutter, vertical = 8.dp),
            ) {
                Box(
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 4.dp, bottom = 8.dp)
                        .testTag("piece:grab")
                        .size(GRAB_W, GRAB_H)
                        .background(Tokens.Colors.lineEdge, CircleShape),
                )
                content()
            }
        }
    }
}
