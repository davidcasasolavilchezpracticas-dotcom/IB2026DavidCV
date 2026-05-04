package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractactionsuccess

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActions
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsViewModel
import com.iberdrola.practicas2026.davidcv.ui.theme.EnergyGreen
import com.iberdrola.practicas2026.davidcv.ui.theme.White

@Composable
fun ContractActionSuccessScreen(
    navController: NavController,
    viewModel: ContractActionsViewModel,
    analytics: FirebaseAnalytics,
) {
    val view = LocalView.current
    val window = (view.context as Activity).window
    val context = LocalContext.current

    DisposableEffect(Unit) {
        onDispose {
            window.statusBarColor = White.toArgb()
            window.navigationBarColor = White.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    SideEffect {
        window.statusBarColor = EnergyGreen.toArgb()
        window.navigationBarColor = EnergyGreen.toArgb()
        WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
    }

    LaunchedEffect(Unit) {
        analytics.logEvent ( "ContractActionSuccessScreen" ) {
            param("eventType", "View")
        }
    }

    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ContractActionSuccessContent(
            state = state,
            censurator = viewModel::censurator,
            onClose = {
                navController.navigate(Routes.CONTRACTS) {
                    popUpTo(Routes.CONTRACTS) {
                        inclusive = true
                    }
                }

                analytics.logEvent ( "ButtonClose" ) {
                    param("eventType", "Click")
                }
            },
            onAccept = {
                navController.navigate(Routes.CONTRACTS) {
                    popUpTo(Routes.CONTRACTS) {
                        inclusive = true
                    }
                }

                analytics.logEvent ( "ButtonAccept" ) {
                    param("eventType", "Click")
                }
            }
        )
    }
}
