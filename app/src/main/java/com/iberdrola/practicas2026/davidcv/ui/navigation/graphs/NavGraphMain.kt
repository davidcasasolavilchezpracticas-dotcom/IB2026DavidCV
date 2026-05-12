package com.iberdrola.practicas2026.davidcv.ui.navigation.graphs

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
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
    viewSelected: Boolean,
    isProcessing: Boolean,
) {
    composable(Routes.LIST_LIGHT) {
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
        )
    }

    composable(Routes.LIST_GAS) {
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
        )
    }

    composable(Routes.INITIAL) {
        InitialScreen(
            navController = navController,
            remoteConfig = remoteConfig,
            onNavigate = onNavigate,
            analytics = analytics,
            modifier = Modifier,
        )
    }

    composable(Routes.FILTER) {
        FilterScreen(
            navController = navController,
            onBack = { safeBack(false) },
            isProcesing = isProcessing,
            analytics = analytics,
        )
    }

    composable(Routes.CONTRACTS) {
        ContractListScreen(
            onBack = { safeBack(false) },
            remoteConfig = remoteConfig,
            onNavigate = onNavigate,
            analytics = analytics,
        )
    }
}

