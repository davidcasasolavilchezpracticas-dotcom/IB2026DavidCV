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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractStatus
import com.iberdrola.practicas2026.davidcv.ui.base.screens.LoadingScreen
import com.iberdrola.practicas2026.davidcv.ui.navigation.DataStoreViewModel
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActions
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActions.*
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsViewModel

@Composable
fun ContractVerifyScreen(
    navController: NavController,
    viewModel: ContractActionsViewModel,
    analytics: FirebaseAnalytics,
) {
    val dataStoreViewModel: DataStoreViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        analytics.logEvent ( "ContractVerifyScreen" ) {
            param("eventType", "View")
        }
    }

    val state by viewModel.state.collectAsState()

    val events = ContractVerifyEvents(
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
                    //viewModel.updateContractStatus(ContractStatus.INACTIVE)
                }
                MODIFYSTATUSEMAIL -> {
                    viewModel.updateContractEmail(state.emailTry)
                }
            }

            navController.navigate(Routes.CONTRACT_SUCCESS)
            analytics.logEvent ( "ButtonNext" ) {
                param("eventType", "Click")
            }
        },
        getTimeLeft = viewModel::getTimeLeft,
        phoneCensurator = viewModel::phoneCensurator,
        createText = viewModel::createText
    )

    Box(modifier = Modifier.fillMaxSize()) {
        ContractVerifyContent(
            dataStoreViewModel = dataStoreViewModel,
            state = state,
            events = events
        )

        LoadingScreen(state.isLoading)
    }
}
