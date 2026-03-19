package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractactivate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.iberdrola.practicas2026.davidcv.ui.navigation.DataStoreViewModel
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsViewModel

@Composable
fun ContractActivateScreen(
    navController: NavController,
    viewModel: ContractActionsViewModel,
    viewModelDS: DataStoreViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val account by viewModelDS.account.collectAsStateWithLifecycle()

    Box(modifier = Modifier.fillMaxSize()) {
        ContractActivateContent(
            state = state,
            account = account,
            onCensurator = viewModel::censurator,
            onEmailChanged = viewModel::onEmailChanged,
            onAcceptedChanged = viewModel::onAcceptedChanged,
            onClose = { navController.navigate(Routes.INITIAL) },
            onBack = { navController.popBackStack() },
            onNext = { navController.navigate(Routes.CONTRACT_VERIFY) }
        )
    }
}
