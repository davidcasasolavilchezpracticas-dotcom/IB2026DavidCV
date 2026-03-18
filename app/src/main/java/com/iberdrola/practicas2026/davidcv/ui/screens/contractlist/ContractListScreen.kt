package com.iberdrola.practicas2026.davidcv.ui.screens.contractlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.exception.ContractException
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.screens.EmptyContractsScreen
import com.iberdrola.practicas2026.davidcv.ui.base.screens.ErrorScreen
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContractListScreen(
    viewModel: ContractListViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val state = viewModel.contractsState.collectAsState()

    Scaffold(
        topBar = {
            Column {
                Text(
                    text = stringResource(R.string.clsTitle),
                    modifier = Modifier.padding(horizontal = LocalSpacing.current.lg, vertical = LocalSpacing.current.sm),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { padding ->
        when (state.value) {
            is ContractListState.Error -> {
                ErrorScreen(
                    message = (state.value as ContractListState.Error).exception.message ?: R.string.blcUnknownError.toString(),
                    modifier = Modifier,
                    img = if ((state.value as ContractListState.Error).exception is ContractException.ConexionFailed) Icons.Default.WifiOff else Icons.Default.Error,
                    onClick = {
                        navController.popBackStack()
                    }
                )
            }
            is ContractListState.Success -> {
                val contracts = (state.value as ContractListState.Success).contracts
                if (contracts.isEmpty()) {
                    EmptyContractsScreen(
                        modifier = Modifier,
                        onRefresh = {
                            navController.navigateUp()
                        }
                    )
                } else {
                    ContractListContent(
                        contracts = contracts,
                        modifier = Modifier.padding(padding),
                        onClick = { id ->
                            navController.navigate(Routes.CONTRACT_ACTIONS + "/$id")
                        }
                    )
                }
            }

            else -> {}
        }


    }
}