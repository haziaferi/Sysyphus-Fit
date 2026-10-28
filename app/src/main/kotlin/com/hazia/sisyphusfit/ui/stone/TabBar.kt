package com.hazia.sisyphusfit.ui.stone

import android.animation.ValueAnimator
import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsPropertyKey
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hazia.sisyphusfit.ui.theme.StoneText
import com.hazia.sisyphusfit.ui.theme.Theme
import com.hazia.sisyphusfit.ui.theme.Tokens
import com.hazia.sisyphusfit.ui.theme.sisifo
import kotlin.math.hypot
import kotlin.math.max
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

data class TabItem(val label: String, @DrawableRes val icon: Int)

/** Where the valley sits, in px: its centre, the floor's width and the floor's depth from the bar's top. */
data class Valley(val centre: Float, val floor: Float, val floorY: Float)

/** The valley's current centre in px, for tests of its motion. */
val ValleyCentre = SemanticsPropertyKey<Float>("ValleyCentre")

// The fitted tab bar (owner 2026-10-28, research vl_tabfit.py): the floor as wide as the current label, never under 60 dp,
// 7 dp below it; walls 32 dp across; labels wrap when within 4 dp of their tab, or when a valley would come within 2 dp
// of an icon or a label. Tabs keep 10 dp above the icon and below the label; the icon sits in a 56 x 32 area, 4 dp above
// the label; the bar keeps 4 dp at each side. The wall's curve is drawn on a 72-high grid whose floor is at 69.
private const val FLOOR_MIN = 60f
private const val BELOW_LABEL = 7f
private const val SPAN = 32f
private const val EDGE = 4f
private const val CLEAR = 2f
private const val TAB_PAD = 10f
private const val DOT_W = 56f
private const val DOT_H = 32f
private const val ICON = 24f
private const val LABEL_GAP = 4f
private const val BAR_SIDE = 4f
private const val UNDERLINE_INSET = 4f
private val TAB_LINE = 16.sp
private val WALL = listOf(56f, 30f, 22f, 18f, 15f, 12f).map { it * SPAN / 56f }

/** The valley under a tab whose label is [labelWidth] wide and ends at [labelBottom] (px). */
fun valleyTarget(centre: Float, labelWidth: Float, labelBottom: Float, density: Float) =
    Valley(centre, max(FLOOR_MIN * density, labelWidth), labelBottom + BELOW_LABEL * density)

/** The valley line as points (px), from far left to far right, 1 dp below the bar's top outside the valley. */
fun valleyPoints(v: Valley, density: Float): List<Offset> {
    val d = density
    fun s(y: Float) = d + (y * d - d) * (v.floorY - d) / (68f * d)
    val e = v.centre - v.floor / 2
    val g = v.centre + v.floor / 2
    val a = WALL.map { it * d }
    val segs = listOf(
        listOf(Offset(e - a[0], d), Offset(e - a[1], d), Offset(e - a[2], s(18f)), Offset(e - a[3], s(40f))),
        listOf(Offset(e - a[3], s(40f)), Offset(e - a[4], s(58f)), Offset(e - a[5], s(69f)), Offset(e, s(69f))),
        listOf(Offset(g, s(69f)), Offset(g + a[5], s(69f)), Offset(g + a[4], s(58f)), Offset(g + a[3], s(40f))),
        listOf(Offset(g + a[3], s(40f)), Offset(g + a[2], s(18f)), Offset(g + a[1], d), Offset(g + a[0], d)),
    )
    val pts = mutableListOf(Offset(-1e5f, d))
    for (q in segs) for (i in 0..80) {
        val u = i / 80f
        val w = 1 - u
        pts += q[0] * (w * w * w) + q[1] * (3 * w * w * u) + q[2] * (3 * w * u * u) + q[3] * (u * u * u)
    }
    pts += Offset(1e5f, d)
    return pts
}

fun valleyYAt(pts: List<Offset>, x: Float): Float {
    var i = 0
    while (i < pts.size - 2 && pts[i + 1].x < x) i++
    val (x0, y0) = pts[i]
    val (x1, y1) = pts[i + 1]
    return if (x1 == x0) y0 else y0 + (x - x0) / (x1 - x0) * (y1 - y0)
}

/** The closest the line comes to any box, less half the line's width (px). */
private fun clearance(pts: List<Offset>, boxes: List<Rect>, density: Float): Float {
    var m = Float.MAX_VALUE
    val half = Tokens.Stroke.line.value * density / 2
    for (b in boxes) {
        var x = b.left - 3 * density
        while (x <= b.right + 3 * density) {
            val y = valleyYAt(pts, x)
            val dx = if (x < b.left) b.left - x else if (x > b.right) x - b.right else 0f
            val dy = if (y < b.top) b.top - y else if (y > b.bottom) y - b.bottom else 0f
            m = minOf(m, hypot(dx, dy) - half)
            x += 0.5f * density
        }
    }
    return m
}

/** Reduced motion, as Android sets it: animations off (animator duration scale 0). */
fun reducedMotion(): Boolean = !ValueAnimator.areAnimatorsEnabled()

/**
 * The bottom tab bar (SPEC 0.29, 0.53, 0.54): fixed at the bottom, its line dipping into a valley under the current tab,
 * the valley's floor underlined. Tapping a tab slides the valley there (`motion.valley`); with reduced motion it jumps.
 * The bar grows with its labels and reaches under the navigation bar ([insets]).
 */
@Composable
fun TabBar(
    tabs: List<TabItem>,
    selected: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
    insets: WindowInsets = WindowInsets.navigationBars,
    reducedMotion: Boolean = reducedMotion(),
) {
    val density = LocalDensity.current
    val d = density.density
    val measurer = rememberTextMeasurer()
    val base = Theme.type.label.copy(lineHeight = TAB_LINE, textAlign = TextAlign.Center)
    val carve = Theme.type.roles.label.carve
    fun style(on: Boolean): TextStyle = if (on) {
        base.copy(color = Tokens.Colors.inkPrimary, fontWeight = FontWeight(500), fontFamily = sisifo(500, carve))
    } else base.copy(color = Tokens.Colors.inkSecondary)
    val bottomInset = insets.getBottom(density)

    BoxWithConstraints(modifier.fillMaxWidth()) {
        val width = constraints.maxWidth.toFloat()
        val tabW = (width - 2 * BAR_SIDE * d) / tabs.size
        fun centre(i: Int) = BAR_SIDE * d + tabW * (i + 0.5f)

        // One line each, then the wrap test of vl_tabfit; wrapped labels take their widest word's width ("This / week").
        fun layoutFor(wrap: Boolean, i: Int, on: Boolean) = if (!wrap) {
            measurer.measure(tabs[i].label, style(on), softWrap = false, maxLines = 1)
        } else {
            val words = tabs[i].label.split(" ").maxOf { measurer.measure(it, style(on), softWrap = false).size.width }
            measurer.measure(tabs[i].label, style(on), constraints = Constraints(maxWidth = words))
        }
        fun boxes(wrap: Boolean, on: Int): List<Rect> = tabs.indices.flatMap { i ->
            val l = layoutFor(wrap, i, i == on).size
            val c = centre(i)
            val labelTop = (TAB_PAD + DOT_H + LABEL_GAP) * d
            listOf(
                Rect(c - ICON * d / 2, (TAB_PAD + (DOT_H - ICON) / 2) * d, c + ICON * d / 2, (TAB_PAD + (DOT_H + ICON) / 2) * d),
                Rect(c - l.width / 2f, labelTop, c + l.width / 2f, labelTop + l.height),
            )
        }
        fun target(wrap: Boolean, i: Int): Valley {
            val l = layoutFor(wrap, i, true).size
            return valleyTarget(centre(i), l.width.toFloat(), (TAB_PAD + DOT_H + LABEL_GAP) * d + l.height, d)
        }
        val wrap = remember(tabs, width, base, d) {
            tabs.indices.any { i ->
                layoutFor(false, i, true).size.width > tabW - 2 * EDGE * d ||
                    clearance(valleyPoints(target(false, i), d), boxes(false, i), d) < CLEAR * d
            }
        }
        val labelH = tabs.indices.maxOf { layoutFor(wrap, it, it == selected).size.height }
        val barH = max(Theme.size.tabBar.value * d, (TAB_PAD + DOT_H + LABEL_GAP + TAB_PAD) * d + labelH)
        val goal = target(wrap, selected)

        val c = remember { Animatable(goal.centre) }
        val f = remember { Animatable(goal.floor) }
        val y = remember { Animatable(goal.floorY) }
        LaunchedEffect(goal, reducedMotion) {
            val spec = tween<Float>(Tokens.Motion.valley.durationMs, easing = Tokens.Motion.valley.easing!!)
            if (reducedMotion) {
                c.snapTo(goal.centre); f.snapTo(goal.floor); y.snapTo(goal.floorY)
            } else {
                coroutineScope {
                    launch { c.animateTo(goal.centre, spec) }
                    launch { f.animateTo(goal.floor, spec) }
                    launch { y.animateTo(goal.floorY, spec) }
                }
            }
        }
        val now = Valley(c.value, f.value, y.value)

        Row(
            Modifier
                .fillMaxWidth()
                .testTag("tabbar")
                .semantics { this[ValleyCentre] = now.centre }
                .drawBehind { drawValley(now, size.width, size.height, d) }
                .padding(start = BAR_SIDE.dp, end = BAR_SIDE.dp, bottom = with(density) { bottomInset.toDp() }),
        ) {
            tabs.forEachIndexed { i, tab ->
                val on = i == selected
                Box(
                    Modifier
                        .weight(1f)
                        .height(with(density) { barH.toDp() })
                        .selectable(on, role = Role.Tab) { onSelect(i) },
                    contentAlignment = Alignment.TopCenter,
                ) {
                    Column(Modifier.padding(top = TAB_PAD.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(Modifier.size(DOT_W.dp, DOT_H.dp), contentAlignment = Alignment.Center) {
                            StoneIcon(tab.icon, tint = if (on) Tokens.Colors.inkPrimary else Tokens.Colors.inkSecondary)
                        }
                        Spacer(Modifier.height(LABEL_GAP.dp))
                        val w = layoutFor(wrap, i, on).size.width
                        StoneText(tab.label, Modifier.width(with(density) { w.toDp() + 1.dp }), style = style(on), softWrap = wrap)
                    }
                }
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawValley(v: Valley, w: Float, h: Float, d: Float) {
    val pts = valleyPoints(v, d)
    val inner = pts.filter { it.x > 0f && it.x < w }
    val line = Path().apply {
        moveTo(0f, valleyYAt(pts, 0f))
        inner.forEach { lineTo(it.x, it.y) }
        lineTo(w, valleyYAt(pts, w))
    }
    val fill = Path().apply { addPath(line); lineTo(w, h); lineTo(0f, h); close() }
    drawPath(fill, Tokens.Colors.surfaceCard)
    drawPath(line, Tokens.Colors.fillDark, style = Stroke(Tokens.Stroke.line.toPx()))
    val e = v.centre - v.floor / 2 + UNDERLINE_INSET * d
    val g = v.centre + v.floor / 2 - UNDERLINE_INSET * d
    drawLine(Tokens.Colors.fillDark, Offset(e, v.floorY), Offset(g, v.floorY), Tokens.Stroke.underline.toPx(), StrokeCap.Round)
}
