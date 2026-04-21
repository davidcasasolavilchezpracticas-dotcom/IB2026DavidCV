package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractStatus
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes

@Composable
fun ContractActionsScreen(
    contractId: Int,
    navController: NavController,
    viewModel: ContractActionsViewModel = hiltViewModel(),
    analytics: FirebaseAnalytics,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        analytics.logEvent("ContractActionsScreen") {
            param("eventType", "View")
        }
        viewModel.getContract(contractId)
    }

    LaunchedEffect(state.contract) {
        state.contract?.let { contract ->
            val route = if (contract.status == ContractStatus.ACTIVE)
                Routes.CONTRACT_INFO else Routes.CONTRACT_ACTIVATE

            navController.navigate(route)
        }
    }

    BackHandler {
        onBack()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
