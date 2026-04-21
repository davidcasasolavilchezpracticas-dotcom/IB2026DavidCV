package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractactivate

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.iberdrola.practicas2026.davidcv.ui.navigation.DataStoreViewModel
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActions
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsViewModel

@Composable
fun ContractActivateScreen(
    navController: NavController,
    viewModel: ContractActionsViewModel,
    viewModelDS: DataStoreViewModel = hiltViewModel(),
    analytics: FirebaseAnalytics
) {
    val state by viewModel.state.collectAsState()
    val account by viewModelDS.account.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        analytics.logEvent ( "ContractActivateScreen" ) {
            param("eventType", "View")
        }
    }
    BackHandler {
        navController.popBackStack()
        navController.popBackStack()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ContractActivateContent(
            state = state,
            account = account,
            onCensurator = viewModel::censurator,
            onEmailChanged = viewModel::onEmailChanged,
            onAcceptedChanged = viewModel::onAcceptedChanged,
            onClose = {
                navController.navigate(Routes.INITIAL)
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
                navController.popBackStack()
                analytics.logEvent ( "ButtonBack" ) {
                    param("eventType", "Click")
                }
            }
        )
    }
}
