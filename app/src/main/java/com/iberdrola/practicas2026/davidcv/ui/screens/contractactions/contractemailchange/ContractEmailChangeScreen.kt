package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractemailchange

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.navigation.rememberDefaultClickHandler
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsViewModel

@Composable
fun ContractEmailChangeScreen(
    navController: NavController,
    viewModel: ContractActionsViewModel,
    analytics: FirebaseAnalytics
) {
    LaunchedEffect(Unit) {
        analytics.logEvent ( "ContractEmailChangeScreen" ) {
            param("eventType", "View")
        }
    }

    val safeClick = rememberDefaultClickHandler(
        onClick = {
            navController.popBackStack()
        }
    )

    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        ContractEmailChangeContent(
            state = state,
            onEmailChanged = viewModel::onEmailChanged,
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
            onNext = {
                navController.navigate(Routes.CONTRACT_VERIFY)
                analytics.logEvent ( "ButtonNext" ) {
                    param("eventType", "Click")
                }
            },
            onBack = {
                safeClick()
                analytics.logEvent ( "ButtonBack" ) {
                    param("eventType", "Click")
                }
            }
        )

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
