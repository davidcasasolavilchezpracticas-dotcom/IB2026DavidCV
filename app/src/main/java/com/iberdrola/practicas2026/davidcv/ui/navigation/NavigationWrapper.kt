package com.iberdrola.practicas2026.davidcv.ui.navigation

import android.app.Activity
import android.os.Build
import android.view.View
import androidx.activity.SystemBarStyle
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowCompat.enableEdgeToEdge
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.iberdrola.practicas2026.davidcv.ui.base.common.ClickEventManager
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalClickManager
import com.iberdrola.practicas2026.davidcv.ui.base.common.SafeClickTools.Companion.canExecuteMethod
import com.iberdrola.practicas2026.davidcv.ui.base.composables.initial.GeneralTopAppBar
import com.iberdrola.practicas2026.davidcv.ui.navigation.auxiliar.NavigationAnalyticsObserver
import com.iberdrola.practicas2026.davidcv.ui.navigation.auxiliar.UIOverlayManager
import com.iberdrola.practicas2026.davidcv.ui.navigation.auxiliar.handleBackNavigation
import com.iberdrola.practicas2026.davidcv.ui.navigation.auxiliar.rememberNavigationActions
import com.iberdrola.practicas2026.davidcv.ui.theme.EnergyGreen
import com.iberdrola.practicas2026.davidcv.ui.theme.White
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
    colorChanger: (Color, Color) -> Unit,
    analytics: FirebaseAnalytics,
    modifier: Modifier,
) {
    val dataStoreViewModel: DataStoreViewModel = hiltViewModel()
    val bsCounter by dataStoreViewModel.bsCounter.collectAsState()

    var viewSelected by rememberSaveable { mutableStateOf(true) }
    var showThanksDialog by remember { mutableStateOf(false) }
    var showOpinionBS by remember { mutableStateOf(false) }
    var isProcessing by remember { mutableStateOf(false) }
    val view = LocalView.current

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val window = (view.context as Activity).window

    LaunchedEffect(currentRoute) {
        val decorView = window.decorView
        val controller = WindowCompat.getInsetsController(window, view)
        when (currentRoute) {
            Routes.CONTRACT_SUCCESS -> {
                colorChanger(EnergyGreen, EnergyGreen)
                controller.isAppearanceLightNavigationBars = true
            }
            Routes.INITIAL -> {
                colorChanger(EnergyGreen, White)
                controller.isAppearanceLightNavigationBars = true
            }
            else -> {
                colorChanger(White, White)
                controller.isAppearanceLightNavigationBars = true
            }
        }

        decorView.systemUiVisibility = decorView.systemUiVisibility or
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

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

    val clickManager = remember { ClickEventManager() }

    CompositionLocalProvider(LocalClickManager provides clickManager) {
        val manager = LocalClickManager.current

        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                topBar = {
                    GeneralTopAppBar(
                        handleBackNavigation = {
                            canExecuteMethod(
                                manager,
                            ) { safeBack(it) }
                        },
                        currentRoute = currentRoute,
                    )
                },
                modifier = modifier,
            ) { innerPadding ->
                AppNavHost(
                    navController = navController,
                    isProcessing = isProcessing,
                    viewSelected = viewSelected,
                    remoteConfig = remoteConfig,
                    padding = innerPadding,
                    analytics = analytics,
                    actions = navActions,
                    safeBack = safeBack,
                    manager = manager
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
}