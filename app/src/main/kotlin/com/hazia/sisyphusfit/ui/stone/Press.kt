package com.hazia.sisyphusfit.ui.stone

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.hazia.sisyphusfit.ui.theme.Theme

/** At least the density's touch target (`size.hit`, SPEC 0.21: never under 42 dp) in both directions. */
@Composable
fun Modifier.minHit(): Modifier = sizeIn(minWidth = Theme.size.hit, minHeight = Theme.size.hit)

/**
 * A pressable area of at least `size.hit`, with Foundation's default press feedback (pressed states are not drawn yet).
 * [description] names a control that shows no text (an icon), for TalkBack; [label] only describes the action.
 */
@Composable
fun Modifier.press(onClick: () -> Unit, label: String? = null, role: Role = Role.Button, description: String? = null): Modifier =
    minHit()
        .then(if (description != null) Modifier.semantics { contentDescription = description } else Modifier)
        .clickable(onClickLabel = label, role = role, onClick = onClick)
