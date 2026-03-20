package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractactionsuccess

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsViewModel

@Composable
fun ContractActionSuccessScreen(
    navController: NavController,
    viewModel: ContractActionsViewModel,
    analytics: FirebaseAnalytics
) {
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
                navController.navigate(Routes.INITIAL)
                analytics.logEvent ( "ButtonClose" ) {
                    param("eventType", "Click")
                }
            },
            onAccept = {
                navController.navigate(Routes.INITIAL)
                analytics.logEvent ( "ButtonAccept" ) {
                    param("eventType", "Click")
                }
            }
        )
    }
}
