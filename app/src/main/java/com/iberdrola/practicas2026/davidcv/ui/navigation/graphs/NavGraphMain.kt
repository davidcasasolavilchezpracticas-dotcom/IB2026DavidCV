package com.iberdrola.practicas2026.davidcv.ui.navigation.graphs

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.iberdrola.practicas2026.davidcv.ui.base.common.ClickEventManager
import com.iberdrola.practicas2026.davidcv.ui.base.transitions.ScaleEnter
import com.iberdrola.practicas2026.davidcv.ui.base.transitions.ScaleExit
import com.iberdrola.practicas2026.davidcv.ui.base.transitions.ScalePopEnter
import com.iberdrola.practicas2026.davidcv.ui.base.transitions.ScalePopExit
import com.iberdrola.practicas2026.davidcv.ui.base.transitions.VerticalEnter
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.screens.billfilter.FilterScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.billlist.BillListScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractlist.ContractListScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.initial.InitialScreen

fun NavGraphBuilder.mainGraph(
    onNavigatePopUpTo: (String, String) -> Unit,
    remoteConfig: FirebaseRemoteConfig,
    navController: NavHostController,
    analytics: FirebaseAnalytics,
    onNavigate: (String) -> Unit,
    safeBack: (Boolean) -> Unit,
    manager: ClickEventManager,
    viewSelected: Boolean,
    isProcessing: Boolean,
) {
    composable(
        popEnterTransition = { ScalePopEnter() },
        popExitTransition = { ScalePopExit() },
        enterTransition = { ScaleEnter() },
        exitTransition = { ScaleExit() },
        route = Routes.LIST_LIGHT,
    ) {
        BillListScreen(
            onNavigatePopUpTo = onNavigatePopUpTo,
            navController = navController,
            onBack = { safeBack(false) },
            viewSelected = viewSelected,
            remoteConfig = remoteConfig,
            isClosing = isProcessing,
            onNavigate = onNavigate,
            analytics = analytics,
            modifier = Modifier,
            manager = manager
        )
    }

    composable(
        popEnterTransition = { ScalePopEnter() },
        popExitTransition = { ScalePopExit() },
        enterTransition = { ScaleEnter() },
        exitTransition = { ScaleExit() },
        route = Routes.LIST_GAS,
    ) {
        BillListScreen(
            onNavigatePopUpTo = onNavigatePopUpTo,
            navController = navController,
            onBack = { safeBack(false) },
            viewSelected = !viewSelected,
            remoteConfig = remoteConfig,
            isClosing = isProcessing,
            onNavigate = onNavigate,
            analytics = analytics,
            modifier = Modifier,
            manager = manager
        )
    }

    composable(
        popEnterTransition = { ScalePopEnter() },
        popExitTransition = { ScalePopExit() },
        enterTransition = { ScaleEnter() },
        exitTransition = { ScaleExit() },
        route = Routes.INITIAL,
    ) {
        InitialScreen(
            remoteConfig = remoteConfig,
            onNavigate = onNavigate,
            analytics = analytics,
            modifier = Modifier,
            manager = manager
        )
    }

    composable(
        popEnterTransition = { VerticalEnter() },
        popExitTransition = { ScalePopExit() },
        enterTransition = { VerticalEnter() },
        exitTransition = { ScaleExit() },
        route = Routes.FILTER,
    ) {
        FilterScreen(
            navController = navController,
            onBack = { safeBack(false) },
            analytics = analytics,
            manager = manager
        )
    }

    composable(
        popEnterTransition = { ScalePopEnter() },
        popExitTransition = { ScalePopExit() },
        enterTransition = { ScaleEnter() },
        exitTransition = { ScaleExit() },
        route = Routes.CONTRACTS,
    ) {
        ContractListScreen(
            onBack = { safeBack(false) },
            remoteConfig = remoteConfig,
            onNavigate = onNavigate,
            analytics = analytics,
            manager = manager
        )
    }
}

