package com.hazia.sisyphusfit.ui.gallery

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hazia.sisyphusfit.R
import com.hazia.sisyphusfit.ui.stone.CarvedEdge
import com.hazia.sisyphusfit.ui.stone.CheckBox
import com.hazia.sisyphusfit.ui.stone.Eyebrow
import com.hazia.sisyphusfit.ui.stone.Chip
import com.hazia.sisyphusfit.ui.stone.FlatRow
import com.hazia.sisyphusfit.ui.stone.HeaderRow
import com.hazia.sisyphusfit.ui.stone.Pill
import com.hazia.sisyphusfit.ui.stone.Segmented
import com.hazia.sisyphusfit.ui.stone.Stepper
import com.hazia.sisyphusfit.ui.stone.StoneIcon
import com.hazia.sisyphusfit.ui.stone.Switch
import com.hazia.sisyphusfit.ui.stone.Tags
import com.hazia.sisyphusfit.ui.stone.WornCard
import com.hazia.sisyphusfit.ui.stone.carvedLine
import com.hazia.sisyphusfit.ui.theme.StoneText
import com.hazia.sisyphusfit.ui.theme.Theme
import com.hazia.sisyphusfit.ui.theme.Tokens

/** Every shared piece once, with sample content from the decided mockups; the gallery and the tests show it. */
@Composable
fun PieceSamples(modifier: Modifier = Modifier) {
    var weight by remember { mutableIntStateOf(60) }
    var reps by remember { mutableIntStateOf(5) }
    var partials by remember { mutableIntStateOf(0) }
    var checked by remember { mutableStateOf(true) }
    var on by remember { mutableStateOf(false) }
    var segment by remember { mutableIntStateOf(0) }
    Column(modifier) {
        HeaderRow("Tuesday morning", clock = "12:40", onBack = {}, onFinish = {})
        Column(Modifier.padding(horizontal = Theme.space.gutter), verticalArrangement = Arrangement.spacedBy(Theme.space.gap)) {
            StoneText("Bench press", style = Theme.type.exercise)
            Tags(listOf("Strength", "Upper body"))
            Chip("Training frequency", onClick = {})
            Stepper("$weight", "kg", onMinus = { weight-- }, onPlus = { weight++ }, onTapValue = {})
            Stepper("$reps", "reps", onMinus = { reps-- }, onPlus = { reps++ }, onTapValue = {}, pebble = Tokens.Radius.pebbleA)
            Stepper("$partials", "partials", onMinus = { partials-- }, onPlus = { partials++ }, onTapValue = {}, small = true)
            Eyebrow("Modules", Modifier.padding(top = Theme.space.section - Theme.space.gap))
            WornCard(onClick = {}) {
                StoneIcon(R.drawable.ic_module_strength)
                Column(Modifier.weight(1f)) {
                    StoneText("Strength", style = Theme.type.body)
                    StoneText("3 sessions this week", style = Theme.type.label.copy(color = Tokens.Colors.inkSecondary))
                }
                StoneIcon(R.drawable.ic_chevron, tint = Tokens.Colors.inkSecondary)
            }
            FlatRow(onClick = {}) {
                StoneText("Backup folder", Modifier.weight(1f), style = Theme.type.body)
                StoneIcon(R.drawable.ic_chevron, tint = Tokens.Colors.inkSecondary)
            }
            StoneText("Last time · Today", Modifier.carvedLine(CarvedEdge.Bottom).padding(bottom = 6.dp), style = Theme.type.label)
            StoneText("27 October", Modifier.carvedLine(CarvedEdge.Top, faded = true, cut = Tokens.Colors.lineSpindle).padding(top = 8.dp), style = Theme.type.label)
            CheckBox(checked, { checked = it })
            Switch(on, { on = it })
            Segmented(listOf("Balanced", "Compact"), segment, { segment = it })
            Pill("Add a note", onClick = {}, icon = R.drawable.ic_edit)
        }
    }
}
