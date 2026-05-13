package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractactionsuccess

import android.app.Activity
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
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
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.iberdrola.practicas2026.davidcv.ui.base.common.ClickEventManager
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsViewModel
import com.iberdrola.practicas2026.davidcv.ui.theme.EnergyGreen
import com.iberdrola.practicas2026.davidcv.ui.theme.White
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ContractActionSuccessScreen(
    onNavigatePopUpTo: (String, String) -> Unit,
    viewModel: ContractActionsViewModel,
    analytics: FirebaseAnalytics,
    manager: ClickEventManager,
) {
    LaunchedEffect(Unit) {
        analytics.logEvent ( "ContractActionSuccessScreen" ) {
            param("eventType", "View")
        }
    }

    BackHandler {
        onNavigatePopUpTo(Routes.CONTRACTS, Routes.CONTRACTS)
    }

    val state by viewModel.state.collectAsState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        ContractActionSuccessContent(
            state = state,
            manager = manager,
            censurator = viewModel::censurator,
            onClose = {
                onNavigatePopUpTo(Routes.CONTRACTS, Routes.CONTRACTS)
                analytics.logEvent ( "ButtonClose" ) {
                    param("eventType", "Click")
                }
            },
            onAccept = {
                onNavigatePopUpTo(Routes.CONTRACTS, Routes.CONTRACTS)
                analytics.logEvent ( "ButtonAccept" ) {
                    param("eventType", "Click")
                }
            }
        )
    }
}
