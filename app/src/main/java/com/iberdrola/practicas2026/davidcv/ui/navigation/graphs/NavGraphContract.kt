package com.iberdrola.practicas2026.davidcv.ui.navigation.graphs

import android.os.Build
import androidx.navigation.navigation
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.google.firebase.analytics.FirebaseAnalytics
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractStatus
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.navigation.CreateViewModel
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsViewModel
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractverify.ContractVerifyScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractactivate.ContractActivateScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractactiveinfo.ContractActiveInfoScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractemailchange.ContractEmailChangeScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractphonechange.ContractPhoneChangeScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractactionsuccess.ContractActionSuccessScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun NavGraphBuilder.contractGraph(
    onNavigatePopUpTo: (String, String) -> Unit,
    navController: NavHostController,
    analytics: FirebaseAnalytics,
    onNavigate: (String) -> Unit,
    safeBack: (Boolean) -> Unit,
) {
    navigation(
        route = "contract_flow/{contractId}",
        startDestination = Routes.CONTRACT_ACTIONS + "/{contractId}"
    ) {
        composable(Routes.CONTRACT_ACTIONS + "/{contractId}") { entry ->
            val contractId = entry.arguments?.getString("contractId")!!.toInt()
            val viewModel: ContractActionsViewModel = CreateViewModel(entry, navController)

            LaunchedEffect(contractId) {
                viewModel.getContract(contractId)
            }

            val state by viewModel.state.collectAsState()
            var hasNavigated by remember { mutableStateOf(false) }

            LaunchedEffect(state.contract) {
                if (!hasNavigated && state.contract != null && !state.isLoading) {
                    hasNavigated = true
                    val route = if (state.contract!!.status == ContractStatus.ACTIVE) {
                        Routes.CONTRACT_INFO
                    } else {
                        Routes.CONTRACT_ACTIVATE
                    }

                    navController.navigate(route) {
                        popUpTo(Routes.CONTRACT_ACTIONS + "/{contractId}") { inclusive = true }
                    }
                }
            }

            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        composable(Routes.CONTRACT_INFO) { entry ->
            ContractActiveInfoScreen(
                viewModel = CreateViewModel(entry, navController),
                onNavigate = onNavigate,
                analytics = analytics,
                onBack = safeBack
            )
        }

        composable(Routes.CONTRACT_ACTIVATE) { entry ->
            ContractActivateScreen(
                viewModel = CreateViewModel(entry, navController),
                onNavigatePopUpTo = onNavigatePopUpTo,
                onNavigate = onNavigate,
                analytics = analytics,
                onBack = safeBack,
            )
        }

        composable(Routes.CONTRACT_EMAIL_CHANGE) { entry ->
            ContractEmailChangeScreen(
                viewModel = CreateViewModel(entry, navController),
                onNavigatePopUpTo = onNavigatePopUpTo,
                onNavigate = onNavigate,
                analytics = analytics,
                onBack = safeBack
            )
        }

        composable(Routes.CONTRACT_PHONE_CHANGE) { entry ->
            ContractPhoneChangeScreen(
                viewModel = CreateViewModel(entry, navController),
                onNavigatePopUpTo = onNavigatePopUpTo,
                onNavigate = onNavigate,
                analytics = analytics,
                onBack = safeBack
            )
        }

        composable(Routes.CONTRACT_VERIFY) { entry ->
            ContractVerifyScreen(
                viewModel = CreateViewModel(entry, navController),
                onNavigatePopUpTo = onNavigatePopUpTo,
                onNavigate = onNavigate,
                analytics = analytics,
                onBack = safeBack
            )
        }

        composable(Routes.CONTRACT_SUCCESS) { entry ->
            ContractActionSuccessScreen(
                viewModel = CreateViewModel(entry, navController),
                onNavigatePopUpTo = onNavigatePopUpTo,
                analytics = analytics,
            )
        }
    }
}
