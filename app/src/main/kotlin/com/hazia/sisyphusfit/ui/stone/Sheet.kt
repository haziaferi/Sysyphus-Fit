package com.hazia.sisyphusfit.ui.stone

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.hazia.sisyphusfit.ui.theme.Theme
import com.hazia.sisyphusfit.ui.theme.Tokens
import kotlin.math.roundToInt
import kotlinx.coroutines.launch

// The sheet's grab handle in the decided mockups (".sheet .grab": 32 x 4, 4 above and 8 below; body padding 8).
private val GRAB_W = 32.dp
private val GRAB_H = 4.dp
// A drag down past a quarter of the sheet's height closes it; less springs back.
private const val DISMISS_FRACTION = 0.25f

/**
 * A bottom sheet (SPEC 0.16, evidence notes; the labels list): `surface.page` with `radius.sheet` top corners and
 * `elevation.float`, over `shadow.scrim`. Tapping the scrim or dragging the sheet down closes it. It slides with
 * `motion.valley`, the app's one decided slide.
 */
@Composable
fun BottomSheet(visible: Boolean, onDismiss: () -> Unit, modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    val easing = checkNotNull(Tokens.Motion.valley.easing)
    val spec = tween<Float>(Tokens.Motion.valley.durationMs, easing = easing)
    val slide = tween<IntOffset>(Tokens.Motion.valley.durationMs, easing = easing)
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
            val drag = remember { Animatable(0f) }
            val scope = rememberCoroutineScope()
            val height = remember { floatArrayOf(1f) }
            Column(
                Modifier
                    .fillMaxWidth()
                    .onSizeChanged { height[0] = it.height.toFloat() }
                    .offset { IntOffset(0, drag.value.roundToInt()) }
                    .draggable(
                        rememberDraggableState { dy -> scope.launch { drag.snapTo((drag.value + dy).coerceAtLeast(0f)) } },
                        Orientation.Vertical,
                        onDragStopped = {
                            if (drag.value > height[0] * DISMISS_FRACTION) onDismiss() else drag.animateTo(0f, spec)
                        },
                    )
                    .elevation(Tokens.Elevation.float, shape)
                    .background(Tokens.Colors.surfacePage, shape)
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(horizontal = Theme.space.gutter, vertical = 8.dp),
            ) {
                Box(
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(top = 4.dp, bottom = 8.dp)
                        .size(GRAB_W, GRAB_H)
                        .background(Tokens.Colors.lineEdge, CircleShape),
                )
                content()
            }
        }
    }
}
