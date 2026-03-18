package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractStatus
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractactivate.ContractActivateScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractactiveinfo.ContractActiveInfoScreen

@Composable
fun ContractActionsScreen(
    contractId: Int,
    navController: NavController,
    viewModel: ContractActionsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getContract(contractId)
    }

    if (state.contract != null && state.contract!!.status == ContractStatus.ACTIVE) {
        state.action = ContractActions.MODIFYEMAIL
        ContractActiveInfoScreen(
            navController = navController,
            viewModel = viewModel
        )
    } else {
        state.action = ContractActions.MODIFYSTATUSEMAIL
        ContractActivateScreen(
            navController = navController,
            viewModel = viewModel
        )
    }
}