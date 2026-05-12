package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractStatus
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes

@Composable
fun ContractActionsScreen(
    viewModel: ContractActionsViewModel = hiltViewModel(),
    analytics: FirebaseAnalytics,
    onNavigate: (String) -> Unit,
    onBack: () -> Unit,
    contractId: Int,
) {
    val state by viewModel.state.collectAsState()
    var hasNavigated by remember { mutableStateOf(false) }

    LaunchedEffect(contractId) {
        analytics.logEvent("ContractActionsScreen") {
            param("eventType", "View")
        }
        hasNavigated = false
        viewModel.getContract(contractId)
    }

    LaunchedEffect(state.contract) {
        state.contract?.let { contract ->
            if (!hasNavigated && !state.isLoading) {
                hasNavigated = true
                val route = if (contract.status == ContractStatus.ACTIVE) {
                    Routes.CONTRACT_INFO
                } else Routes.CONTRACT_ACTIVATE

                onNavigate(route)
            }
        }
    }

    BackHandler {
        onBack()
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
        Column{
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
