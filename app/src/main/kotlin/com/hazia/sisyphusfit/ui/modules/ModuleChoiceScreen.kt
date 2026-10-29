package com.hazia.sisyphusfit.ui.modules

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.hazia.sisyphusfit.core.Module
import com.hazia.sisyphusfit.ui.stone.BottomZone
import com.hazia.sisyphusfit.ui.stone.CarvedEdge
import com.hazia.sisyphusfit.ui.stone.CheckMark
import com.hazia.sisyphusfit.ui.stone.MainBar
import com.hazia.sisyphusfit.ui.stone.StoneIcon
import com.hazia.sisyphusfit.ui.stone.WornCard
import com.hazia.sisyphusfit.ui.stone.WornShape
import com.hazia.sisyphusfit.ui.stone.carvedLine
import com.hazia.sisyphusfit.ui.stone.elevation
import com.hazia.sisyphusfit.ui.stone.press
import com.hazia.sisyphusfit.ui.theme.StoneText
import com.hazia.sisyphusfit.ui.theme.Theme
import com.hazia.sisyphusfit.ui.theme.Tokens
import kotlin.math.roundToInt

/**
 * First launch (SPEC 0.18): the five modules, none ticked, each with its icon before its name (owner's pick on the 6T);
 * a tap on a card ticks it, its "info" chip opens what it
 * includes in a small card in place (the circled "i" stays the evidence notes', 0.16). "Step forward" records the
 * choice, none ticked included.
 */
@Composable
fun ModuleChoiceScreen(
    onStepForward: (Set<Module>) -> Unit,
    modifier: Modifier = Modifier,
) {
    var ticked by rememberSaveable { mutableStateOf(listOf<String>()) }
    var open by remember { mutableStateOf<Module?>(null) }
    val cards = remember { mutableStateMapOf<Module, Rect>() }
    val marks = remember { mutableStateMapOf<Module, Rect>() }
    var origin by remember { mutableStateOf(Offset.Zero) }
    // 0.54: when any name would wrap beside its chip, every card puts the chip and box under its text (one form per
    // screen); kept until the font size or the width changes, so the wider names cannot flip it back.
    var stacked by remember(LocalDensity.current.fontScale, LocalWindowInfo.current.containerSize.width) { mutableStateOf(false) }
    BackHandler(enabled = open != null) { open = null }

    Box(modifier.fillMaxSize().onGloballyPositioned { origin = it.positionInRoot() }) {
        Column(Modifier.fillMaxSize()) {
            Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(Theme.space.gutter)) {
                StoneText("Start your journey", style = Theme.type.heading)
                Spacer(Modifier.height(Theme.space.gap))
                StoneText(
                    "Choose which paths you want to follow. The other choices will still be available in Settings.",
                    style = Theme.type.body.copy(color = Tokens.Colors.inkSecondary),
                )
                Spacer(Modifier.height(Theme.space.section))
                Column(verticalArrangement = Arrangement.spacedBy(Theme.space.gap)) {
                    Module.entries.forEach { m ->
                        ModuleCard(
                            module = m,
                            checked = m.name in ticked,
                            onChange = { on -> ticked = if (on) ticked + m.name else ticked - m.name },
                            onMark = { open = m },
                            onCard = { cards[m] = it },
                            onMarkPlaced = { marks[m] = it },
                            stacked = stacked,
                            onNameWraps = { stacked = true },
                        )
                    }
                }
            }
            BottomZone {
                MainBar("Step forward", onClick = { onStepForward(Module.entries.filter { it.name in ticked }.toSet()) })
            }
        }
        val shown = open
        val card = shown?.let { cards[it] }
        val at = shown?.let { marks[it] }
        if (shown != null && card != null && at != null) {
            Includes(ModuleTexts.getValue(shown).includes, card.translate(-origin), at.translate(-origin), onClose = { open = null })
        }
    }
}

@Composable
private fun ModuleCard(
    module: Module,
    checked: Boolean,
    onChange: (Boolean) -> Unit,
    onMark: () -> Unit,
    onCard: (Rect) -> Unit,
    onMarkPlaced: (Rect) -> Unit,
    stacked: Boolean,
    onNameWraps: () -> Unit,
) {
    val text = ModuleTexts.getValue(module)
    val title = Theme.type.rowTitle
    val oneLine = with(LocalDensity.current) { title.fontSize.toPx() * title.lineHeight.value / title.fontSize.value }
    val controls: @Composable () -> Unit = {
        Box(
            Modifier
                .testTag("includes:${module.name}")
                .onGloballyPositioned { onMarkPlaced(it.boundsInRoot()) }
                .press(onMark, label = "show what it includes", description = "What ${text.name} includes"),
            contentAlignment = Alignment.Center,
        ) {
            StoneText(
                "info",
                Modifier.carvedLine(CarvedEdge.Top).carvedLine(CarvedEdge.Bottom).padding(horizontal = 8.dp, vertical = 4.dp),
                style = Theme.type.chip,
            )
        }
        Box(Modifier.testTag("box:${module.name}")) { CheckMark(checked) }
    }
    Box(
        Modifier
            .testTag("module:${module.name}")
            .onGloballyPositioned { onCard(it.boundsInRoot()) }
            .toggleable(checked, role = Role.Checkbox, onValueChange = onChange),
    ) {
        WornCard {
            Column(Modifier.weight(1f)) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    StoneIcon(text.icon)
                    StoneText(
                        text.name,
                        Modifier.weight(1f, fill = false).onSizeChanged { if (!stacked && it.height > oneLine * 1.5f) onNameWraps() },
                        style = title,
                    )
                }
                StoneText(text.line, style = Theme.type.body.copy(color = Tokens.Colors.inkSecondary))
                if (module !in BuiltModules) {
                    StoneText(
                        "Arrives in a later update",
                        Modifier.testTag("later:${module.name}"),
                        style = Theme.type.label.copy(color = Tokens.Colors.inkSecondary),
                    )
                }
                if (stacked) {
                    Row(
                        Modifier.align(Alignment.End),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) { controls() }
                }
            }
            if (!stacked) controls()
        }
    }
}

/**
 * What a module includes, in a small raised card over the list: under the module's mark, as wide as its card less the
 * card's padding, above the mark instead when there is no room below. A tap anywhere else, or Back, closes it.
 */
@Composable
private fun Includes(text: String, card: Rect, mark: Rect, onClose: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .testTag("includes-scrim")
            .clickable(interactionSource = null, indication = null, onClickLabel = "close", onClick = onClose),
    )
    val inset = Theme.space.pad
    Box(
        Modifier.fillMaxSize().layout { measurable, constraints ->
            val left = card.left.roundToInt() + inset.roundToPx()
            val width = (card.width.roundToInt() - 2 * inset.roundToPx()).coerceAtLeast(0)
            val p = measurable.measure(Constraints(minWidth = width, maxWidth = width))
            val below = mark.bottom.roundToInt()
            val y = if (below + p.height <= constraints.maxHeight) below else (mark.top.roundToInt() - p.height).coerceAtLeast(0)
            layout(constraints.maxWidth, constraints.maxHeight) { p.place(left, y) }
        },
    ) {
        Box(
            Modifier
                .testTag("includes-card")
                .fillMaxWidth()
                .elevation(Tokens.Elevation.float, WornShape)
                .background(Tokens.Colors.surfaceCard, WornShape)
                .padding(Theme.space.pad),
        ) { StoneText(text, style = Theme.type.body) }
    }
}
