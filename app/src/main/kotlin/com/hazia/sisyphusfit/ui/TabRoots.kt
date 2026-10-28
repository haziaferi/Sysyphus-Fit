package com.hazia.sisyphusfit.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hazia.sisyphusfit.ui.start.StartViewModel
import com.hazia.sisyphusfit.ui.theme.StoneText
import com.hazia.sisyphusfit.ui.theme.Theme

/** A tab's root until its own plan builds the screen: the tab's name as the heading. */
@Composable
internal fun TabRootTitle(tab: Int) {
    StoneText(
        AppTabs[tab].label,
        style = Theme.type.heading,
        modifier = Modifier.fillMaxSize().padding(Theme.space.gutter).testTag("root:$tab"),
    )
}

/** Start's root: its ViewModel is wired now; the screen that draws its state comes with Start's plan. */
@Composable
internal fun StartRootScreen(vm: StartViewModel = viewModel(factory = StartViewModel.Factory)) {
    vm.state.collectAsStateWithLifecycle()
    TabRootTitle(0)
}
