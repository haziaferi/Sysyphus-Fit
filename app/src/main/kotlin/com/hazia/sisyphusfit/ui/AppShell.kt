package com.hazia.sisyphusfit.ui

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.hazia.sisyphusfit.R
import com.hazia.sisyphusfit.ui.stone.TabBar
import com.hazia.sisyphusfit.ui.stone.TabItem
import com.hazia.sisyphusfit.ui.theme.SisyphusTheme
import com.hazia.sisyphusfit.ui.theme.Tokens
import kotlinx.serialization.Serializable

// One nested graph per tab, each with its root; a screen opened inside a tab adds its route to that tab's graph.
@Serializable data object StartTab
@Serializable data object WeekTab
@Serializable data object HistoryTab
@Serializable data object MoreTab

@Serializable data object StartRoot
@Serializable data object WeekRoot
@Serializable data object HistoryRoot
@Serializable data object MoreRoot

/** The bottom tabs (0.29, in English first as 0.39 says). */
val AppTabs = listOf(
    TabItem("Start", R.drawable.ic_tab_start), TabItem("This week", R.drawable.ic_tab_week),
    TabItem("History", R.drawable.ic_tab_history), TabItem("More", R.drawable.ic_tab_more),
)

private val graphs = listOf(StartTab, WeekTab, HistoryTab, MoreTab)
private val roots = listOf(StartRoot, WeekRoot, HistoryRoot, MoreRoot)

/**
 * The app's frame: the current tab's screen above the fixed tab bar (0.54), which shows on the four roots. The app
 * opens on Start (0.25). Changing tab keeps each tab's place (the documented multiple-back-stacks pattern); Back from
 * another tab's root returns to Start, and from Start leaves the app. The screen changes at once (0.53): only the
 * valley moves.
 */
@Composable
fun AppShell() {
    val nav = rememberNavController()
    val entry by nav.currentBackStackEntryAsState()
    val destination = entry?.destination
    val selected = graphs.indexOfFirst { g -> destination?.hierarchy?.any { it.hasRoute(g::class) } == true }.coerceAtLeast(0)
    val onRoot = destination == null || roots.any { destination.hasRoute(it::class) }
    SisyphusTheme {
        Column(Modifier.fillMaxSize().background(Tokens.Colors.surfacePage)) {
            NavHost(
                nav,
                startDestination = StartTab,
                modifier = Modifier.weight(1f).windowInsetsPadding(WindowInsets.statusBars),
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                popEnterTransition = { EnterTransition.None },
                popExitTransition = { ExitTransition.None },
            ) {
                navigation<StartTab>(startDestination = StartRoot) { composable<StartRoot> { StartRootScreen() } }
                navigation<WeekTab>(startDestination = WeekRoot) { composable<WeekRoot> { TabRootTitle(1) } }
                navigation<HistoryTab>(startDestination = HistoryRoot) { composable<HistoryRoot> { TabRootTitle(2) } }
                navigation<MoreTab>(startDestination = MoreRoot) { composable<MoreRoot> { TabRootTitle(3) } }
            }
            if (onRoot) {
                TabBar(AppTabs, selected, onSelect = { i ->
                    nav.navigate(graphs[i]) {
                        popUpTo(nav.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
            }
        }
    }
}
