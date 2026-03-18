package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractverify

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractStatus
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActions
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActions.*
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsViewModel

@Composable
fun ContractVerifyScreen(
    navController: NavController,
    viewModel: ContractActionsViewModel
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        ContractVerifyContent(
            state = state,
            onVerifyCodeChanged = viewModel::onVerifyCodeChanged,
            generateNewCode = viewModel::generateNewCode,
            onClose = { navController.navigate(Routes.INITIAL) },
            onBack = { navController.popBackStack() },
            onNext = {
                when (state.action) {
                    MODIFYEMAIL -> {
                        viewModel.updateContractEmail(state.emailTry)
                    }
                    MODIFYSTATUS -> {
                        viewModel.updateContractStatus(ContractStatus.INACTIVE)
                    }
                    MODIFYSTATUSEMAIL -> {
                        viewModel.updateContractEmailAndStatus(state.emailTry, ContractStatus.ACTIVE)
                    }
                }

                navController.navigate(Routes.CONTRACT_SUCCESS)
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
