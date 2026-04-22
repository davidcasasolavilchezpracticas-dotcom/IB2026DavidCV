package com.iberdrola.practicas2026.davidcv.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsViewModel

@Composable
fun CreateViewModel(
    entry: NavBackStackEntry,
    navController: NavHostController
): ContractActionsViewModel{
    val parentEntry = remember(entry) {
        try { navController.getBackStackEntry("contract_flow/{contractId}") }
        catch (e: Exception) { entry }
    }

    val viewModel: ContractActionsViewModel = hiltViewModel(parentEntry)

    return viewModel
}