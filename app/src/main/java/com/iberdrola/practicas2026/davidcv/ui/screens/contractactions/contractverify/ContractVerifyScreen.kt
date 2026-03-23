package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractverify

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
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractStatus
import com.iberdrola.practicas2026.davidcv.ui.base.screens.LoadingScreen
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActions
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActions.*
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsViewModel

@Composable
fun ContractVerifyScreen(
    navController: NavController,
    viewModel: ContractActionsViewModel,
    analytics: FirebaseAnalytics
) {
    LaunchedEffect(Unit) {
        analytics.logEvent ( "ContractVerifyScreen" ) {
            param("eventType", "View")
        }
    }

    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        ContractVerifyContent(
            state = state,
            onVerifyCodeChanged = viewModel::onVerifyCodeChanged,
            generateNewCode = { context ->
                viewModel.generateNewCode(context)
                analytics.logEvent ( "ButtonNewVerifyCodeGenerated" ) {
                    param("eventType", "Click")
                }
            },
            onLoadEnd = viewModel::onLoadEnd,
            onClose = {
                navController.navigate(Routes.INITIAL)
                analytics.logEvent ( "ButtonClose" ) {
                    param("eventType", "Click")
                }
            },
            onBack = {
                navController.popBackStack()
                analytics.logEvent ( "ButtonBack" ) {
                    param("eventType", "Click")
                }
            },
            onNext = {
                when (state.action) {
                    MODIFYEMAIL -> {
                        viewModel.updateContractEmail(state.emailTry)
                    }
                    MODIFYPHONE -> {
                        viewModel.updateContractPhone(state.phoneTry)
                    }
                    MODIFYSTATUS -> {
                        viewModel.updateContractStatus(ContractStatus.INACTIVE)
                    }
                    MODIFYSTATUSEMAIL -> {
                        viewModel.updateContractEmailAndStatus(state.emailTry, ContractStatus.ACTIVE)
                    }
                }

                navController.navigate(Routes.CONTRACT_SUCCESS)
                analytics.logEvent ( "ButtonNext" ) {
                    param("eventType", "Click")
                }
            }
        )

        LoadingScreen(state.isLoading)
    }
}
