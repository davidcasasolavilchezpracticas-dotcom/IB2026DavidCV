package com.iberdrola.practicas2026.davidcv.ui.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.iberdrola.practicas2026.davidcv.ui.base.composables.initial.GeneralTopAppBar
import com.iberdrola.practicas2026.davidcv.ui.navigation.auxiliar.NavigationAnalyticsObserver
import com.iberdrola.practicas2026.davidcv.ui.navigation.auxiliar.UIOverlayManager
import com.iberdrola.practicas2026.davidcv.ui.navigation.auxiliar.handleBackNavigation
import com.iberdrola.practicas2026.davidcv.ui.navigation.auxiliar.rememberNavigationActions
import kotlinx.coroutines.delay

/**
 * NavigationWrapper
 * Se define el contenedor del grafo de navegación con Scaffold integrado para la TopBar y lógica de navegación centralizada.
 */
@Composable
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun NavigationWrapper(
    remoteConfig: FirebaseRemoteConfig,
    navController: NavHostController,
    analytics: FirebaseAnalytics,
    modifier: Modifier,
) {
    val dataStoreViewModel: DataStoreViewModel = hiltViewModel()
    val bsCounter by dataStoreViewModel.bsCounter.collectAsState()

    // UI States
    var viewSelected by rememberSaveable { mutableStateOf(true) }
    var showThanksDialog by remember { mutableStateOf(false) }
    var showOpinionBS by remember { mutableStateOf(false) }
    var isProcessing by remember { mutableStateOf(false) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Debounce processing logic
    LaunchedEffect(isProcessing) {
        if (isProcessing) {
            delay(800)
            isProcessing = false
        }
    }

    NavigationAnalyticsObserver(navController, analytics)

    val navActions = rememberNavigationActions(navController, isProcessing) { isProcessing = it }

    val safeBack: (Boolean) -> Unit = { double ->
        if (!isProcessing) {
            handleBackNavigation(
                onNavigationStart = { isProcessing = true },
                onNavigationEnd = { isProcessing = false },
                onShowOpinionBS = { showOpinionBS = true },
                dataStoreViewModel = dataStoreViewModel,
                navController = navController,
                currentRoute = currentRoute,
                bsCounter = bsCounter,
                analytics = analytics,
                doubleBack = double,
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = modifier,
            topBar = {
                GeneralTopAppBar(
                    handleBackNavigation = safeBack,
                    currentRoute = currentRoute,
                )
            }
        ) { innerPadding ->
            AppNavHost(
                navController = navController,
                isProcessing = isProcessing,
                viewSelected = viewSelected,
                remoteConfig = remoteConfig,
                padding = innerPadding,
                analytics = analytics,
                actions = navActions,
                safeBack = safeBack
            )
        }

        UIOverlayManager(
            isProcessing = isProcessing,
            navController = navController,
            showOpinionBS = showOpinionBS,
            showThanksDialog = showThanksDialog,
            onOpinionDismiss = { showOpinionBS = false },
            onThanksDismiss = { showThanksDialog = false },
            onLater = { dataStoreViewModel.updateBsCounter(3) },
            onRated = {
                dataStoreViewModel.updateBsCounter(10)
                showThanksDialog = true
            },
        )
    }
}

