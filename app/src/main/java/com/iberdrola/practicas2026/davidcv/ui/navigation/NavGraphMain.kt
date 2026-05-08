package com.iberdrola.practicas2026.davidcv.ui.navigation

import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.iberdrola.practicas2026.davidcv.ui.screens.billfilter.FilterScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.billlist.BillListScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractlist.ContractListScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.initial.InitialScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.useraccount.EditProfileScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.useraccount.UserAccountScreen

fun NavGraphBuilder.mainGraph(
    viewSelected: Boolean,
    isProcessing: Boolean,
    navController: NavHostController,
    analytics: FirebaseAnalytics,
    remoteConfig: FirebaseRemoteConfig,
    safeClick: () -> Unit
) {
    composable(Routes.ACCOUNT_INFO) {
        UserAccountScreen(
            navController = navController,
            analytics = analytics,
            onBack = {
                safeClick()
            }
        )
    }

    composable(Routes.ACCOUNT_EDIT) {
        EditProfileScreen(
            navController = navController,
            analytics = analytics,
            onBack = safeClick
        )
    }

    composable(Routes.LIST_LIGHT) {
        BillListScreen(
            navController = navController,
            viewSelected = viewSelected,
            analytics = analytics,
            remoteConfig = remoteConfig,
            isClosing = isProcessing,
            onBack = {
                safeClick()
            },
            modifier = Modifier
        )
    }

    composable(Routes.LIST_GAS) {
        BillListScreen(
            navController = navController,
            viewSelected = !viewSelected,
            analytics = analytics,
            remoteConfig = remoteConfig,
            isClosing = isProcessing,
            onBack = {
                safeClick()
            },
            modifier = Modifier
        )
    }

    composable(Routes.INITIAL) {
        InitialScreen(
            navController = navController,
            modifier = Modifier,
            analytics = analytics,
            remoteConfig = remoteConfig
        )
    }

    composable(Routes.FILTER) {
        FilterScreen(
            navController = navController,
            analytics = analytics,
            onBack = {
                safeClick()
            }
        )
    }

    composable(Routes.CONTRACTS) {
        ContractListScreen(
            navController = navController,
            remoteConfig = remoteConfig,
            analytics = analytics,
            onBack = {
                safeClick()
            }
        )
    }
}

