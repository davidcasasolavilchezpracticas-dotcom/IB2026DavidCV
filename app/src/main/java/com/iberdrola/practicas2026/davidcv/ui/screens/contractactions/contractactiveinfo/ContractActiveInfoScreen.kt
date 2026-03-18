package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractactiveinfo

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractStatus
import com.iberdrola.practicas2026.davidcv.ui.base.screens.ErrorScreen
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActions
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsViewModel

@Composable
fun ContractActiveInfoScreen(
    navController: NavController,
    viewModel: ContractActionsViewModel
) {
    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            state.errorMessage != null -> {
                ErrorScreen(
                    message = state.errorMessage!!,
                    img = Icons.Default.ErrorOutline,
                    onClick = { navController.popBackStack() }
                )
            }
            state.contract != null -> {
                ContractActiveInfoContent(
                    contract = state.contract!!,
                    onModifyEmail = {
                        navController.navigate(Routes.CONTRACT_EMAIL_CHANGE)
                        state.action = ContractActions.MODIFYEMAIL
                    },
                    onDesactivate = {
                        navController.navigate(Routes.CONTRACT_VERIFY)
                        state.action = ContractActions.MODIFYSTATUS
                    }
                )
            }
        }
    }
}
