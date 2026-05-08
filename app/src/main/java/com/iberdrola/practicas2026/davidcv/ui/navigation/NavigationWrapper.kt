package com.iberdrola.practicas2026.davidcv.ui.navigation

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.base.composables.initial.GeneralTopAppBar
import com.iberdrola.practicas2026.davidcv.ui.base.composables.navigation.BlockingOverlay
import com.iberdrola.practicas2026.davidcv.ui.base.composables.navigation.OpinionManager
import com.iberdrola.practicas2026.davidcv.ui.base.screens.OpinionBottomSheet
import com.iberdrola.practicas2026.davidcv.ui.base.screens.ThanksForRatingDialog
import com.iberdrola.practicas2026.davidcv.ui.screens.billfilter.FilterScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.billlist.BillListScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractactionsuccess.ContractActionSuccessScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractactivate.ContractActivateScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractactiveinfo.ContractActiveInfoScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractemailchange.ContractEmailChangeScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractphonechange.ContractPhoneChangeScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractverify.ContractVerifyScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractlist.ContractListScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.initial.InitialScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.useraccount.EditProfileScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.useraccount.UserAccountScreen
import java.util.Locale

/**
 * NavigationWrapper
 * Se define el contenedor del grafo de navegación con Scaffold integrado para la TopBar y lógica de navegación centralizada.
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationWrapper(
    modifier: Modifier,
    navController: NavHostController,
    remoteConfig: FirebaseRemoteConfig,
    analytics: FirebaseAnalytics
) {
    val dataStoreViewModel: DataStoreViewModel = hiltViewModel()
    val bsCounter by dataStoreViewModel.bsCounter.collectAsState()

    // Estados de UI controlados
    var isProcessing by remember { mutableStateOf(false) }
    var showOpinionBS by remember { mutableStateOf(false) }
    var showThanksDialog by remember { mutableStateOf(false) }
    var viewSelected by rememberSaveable { mutableStateOf(true) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Efecto de bloqueo
    LaunchedEffect(isProcessing) {
        if (isProcessing) {
            delay(800)
            isProcessing = false
        }
    }

    NavigationAnalyticsObserver(navController, analytics)

    // Lógica centralizada de clic seguro
    val safeClick = rememberDefaultClickHandler {
        if (!isProcessing) {
            handleBackNavigation(
                currentRoute = currentRoute,
                navController = navController,
                bsCounter = bsCounter,
                dataStoreViewModel = dataStoreViewModel,
                analytics = analytics,
                onNavigationStart = { isProcessing = true },
                onNavigationEnd = { isProcessing = false },
                onShowOpinionBS = { showOpinionBS = true }
            )
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = modifier,
            topBar = {
                GeneralTopAppBar(currentRoute, navController, handleBackNavigation = safeClick)
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Routes.INITIAL,
                modifier = Modifier.padding(innerPadding)
            ) {
                mainGraph(viewSelected, isProcessing, navController, analytics, remoteConfig, safeClick)
                contractGraph(navController, analytics, safeClick)
            }
        }

        // Modales y Overlays
        OpinionManager(
            showBS = showOpinionBS,
            showThanks = showThanksDialog,
            onDismissBS = { showOpinionBS = false },
            onDismissThanks = { showThanksDialog = false },
            onLater = { dataStoreViewModel.updateBsCounter(3) },
            onRated = { dataStoreViewModel.updateBsCounter(10); showThanksDialog = true },
            navController = navController
        )

        if (isProcessing) {
            BlockingOverlay()
        }
    }
}