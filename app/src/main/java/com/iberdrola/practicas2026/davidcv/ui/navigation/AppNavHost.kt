package com.iberdrola.practicas2026.davidcv.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.iberdrola.practicas2026.davidcv.ui.navigation.auxiliar.NavigationActions
import com.iberdrola.practicas2026.davidcv.ui.navigation.graphs.contractGraph
import com.iberdrola.practicas2026.davidcv.ui.navigation.graphs.mainGraph

@Composable
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun AppNavHost(
    remoteConfig: FirebaseRemoteConfig,
    navController: NavHostController,
    analytics: FirebaseAnalytics,
    safeBack: (Boolean) -> Unit,
    actions: NavigationActions,
    padding: PaddingValues,
    isProcessing: Boolean,
    viewSelected: Boolean,
) {
    NavHost(
        modifier = Modifier.padding(padding),
        startDestination = Routes.INITIAL,
        navController = navController,
    ) {
        mainGraph(
            onNavigatePopUpTo = actions::navigatePopUpTo,
            onNavigate = actions::navigate,
            navController = navController,
            viewSelected = viewSelected,
            isProcessing = isProcessing,
            remoteConfig = remoteConfig,
            analytics = analytics,
            safeBack = safeBack,
        )
        contractGraph(
            onNavigatePopUpTo = actions::navigatePopUpTo,
            onNavigate = actions::navigate,
            navController = navController,
            analytics = analytics,
            safeBack = safeBack,
        )
    }
}