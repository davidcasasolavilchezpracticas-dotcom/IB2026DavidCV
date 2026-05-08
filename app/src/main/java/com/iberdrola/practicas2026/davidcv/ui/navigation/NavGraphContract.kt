package com.iberdrola.practicas2026.davidcv.ui.navigation

import android.os.Build
import androidx.navigation.navigation
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.google.firebase.analytics.FirebaseAnalytics
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractverify.ContractVerifyScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractactiveinfo.ContractActiveInfoScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractactionsuccess.ContractActionSuccessScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractactivate.ContractActivateScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractphonechange.ContractPhoneChangeScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractemailchange.ContractEmailChangeScreen

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun NavGraphBuilder.contractGraph(
    navController: NavHostController,
    analytics: FirebaseAnalytics,
    safeClick: () -> Unit
) {
    navigation(
        route = "contract_flow/{contractId}",
        startDestination = Routes.CONTRACT_ACTIONS + "/{contractId}"
    ) {
        composable(Routes.CONTRACT_ACTIONS + "/{contractId}") { entry ->
            ContractActionsScreen(
                contractId = entry.arguments?.getString("contractId")!!.toInt(),
                navController = navController,
                viewModel = CreateViewModel(entry, navController),
                analytics = analytics,
                onBack = {
                    safeClick()
                }
            )
        }

        composable(Routes.CONTRACT_INFO) { entry ->
            ContractActiveInfoScreen(
                navController = navController,
                viewModel = CreateViewModel(entry, navController),
                analytics = analytics
            )
        }

        composable(Routes.CONTRACT_ACTIVATE) { entry ->
            ContractActivateScreen(
                navController = navController,
                viewModel = CreateViewModel(entry, navController),
                analytics = analytics
            )
        }

        composable(Routes.CONTRACT_EMAIL_CHANGE) { entry ->
            ContractEmailChangeScreen(
                navController = navController,
                viewModel = CreateViewModel(entry, navController),
                analytics = analytics,
            )
        }

        composable(Routes.CONTRACT_PHONE_CHANGE) { entry ->
            ContractPhoneChangeScreen(
                navController = navController,
                viewModel = CreateViewModel(entry, navController),
                analytics = analytics,
            )
        }

        composable(Routes.CONTRACT_VERIFY) { entry ->
            ContractVerifyScreen(
                navController = navController,
                viewModel = CreateViewModel(entry, navController),
                analytics = analytics,
            )
        }

        composable(Routes.CONTRACT_SUCCESS) { entry ->
            ContractActionSuccessScreen(
                navController = navController,
                viewModel = CreateViewModel(entry, navController),
                analytics = analytics,
            )
        }
    }
}
