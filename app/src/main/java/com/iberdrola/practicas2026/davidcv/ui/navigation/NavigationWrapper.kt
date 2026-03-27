package com.iberdrola.practicas2026.davidcv.ui.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.base.screens.OpinionBottomSheet
import com.iberdrola.practicas2026.davidcv.ui.screens.billfilter.FilterScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.billlist.BillListScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsViewModel
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractactionsuccess.ContractActionSuccessScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractemailchange.ContractEmailChangeScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractphonechange.ContractPhoneChangeScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractverify.ContractVerifyScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractlist.ContractListScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.initial.InitialScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.useraccount.EditProfileScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.useraccount.UserAccountScreen
import java.util.Locale

/**
 * NavigationWrapper
 * Se define el contenedor del grafo de navegación
 *
 * @param navController
 * @param modifier
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationWrapper(
    modifier: Modifier,
    navController: NavHostController,
    remoteConfig: FirebaseRemoteConfig,
    analytics: FirebaseAnalytics
) {
    val context = LocalContext.current
    val dataStoreViewModel: DataStoreViewModel = hiltViewModel()
    val bsCounter by dataStoreViewModel.bsCounter.collectAsState()
    var viewSelected by rememberSaveable { mutableStateOf(true) }
    var showOpinionBS by remember { mutableStateOf(false) }

    // 2. MOSTRAR EL BOTTOM SHEET SI EL ESTADO ES TRUE
    if (showOpinionBS) {
        OpinionBottomSheet(
            onDismiss = { showOpinionBS = false },
            onLaterClick = {
                dataStoreViewModel.updateBsCounter(3)
                showOpinionBS = false
                navController.popBackStack() // Ahora sí volvemos atrás
            },
            onRatingSelected = {
                Toast.makeText(context, R.string.bsToast, Toast.LENGTH_SHORT).show()
                dataStoreViewModel.updateBsCounter(10)
                showOpinionBS = false
                navController.popBackStack()
            }
        )
    }


    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { navegator, destination, _ ->
            val route = destination.route ?: "unknown"
            val originRoute = navegator.previousBackStackEntry?.destination?.route ?: "Start"
            val eventName = "From${ originRoute.capitalize(Locale.getDefault()) }To${ route.capitalize(Locale.getDefault()) }"

            // Logueamos la vista de pantalla
            analytics.logEvent(eventName) {
                param("eventType", "Movement")
            }
        }

        navController.addOnDestinationChangedListener(listener)

        // Limpieza al destruir el Composable
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }


    NavHost(
        navController = navController,
        startDestination = Routes.INITIAL,
        modifier = modifier
    ) {

        composable(
            Routes.ACCOUNT_INFO
        ) {
            UserAccountScreen(
                navController = navController,
                analytics = analytics
            )
        }


        composable(
            Routes.ACCOUNT_EDIT
        ) {
            EditProfileScreen(
                navController = navController,
                analytics = analytics
            )
        }

        composable(
            Routes.LIST_LIGHT
        ) {
            BillListScreen(
                modifier = Modifier,
                navController = navController,
                viewSelected = viewSelected,
                analytics = analytics,
                remoteConfig = remoteConfig
            )
        }

        composable(
            Routes.LIST_GAS
        ) {
            BillListScreen(
                modifier = Modifier,
                navController = navController,
                viewSelected = !viewSelected,
                analytics = analytics,
                remoteConfig = remoteConfig
            )
        }

        composable(
            Routes.INITIAL
        ) {
            InitialScreen(
                navController = navController,
                modifier = Modifier,
                analytics = analytics,
                remoteConfig = remoteConfig
            )
        }

        composable(
            Routes.FILTER
        ) {
            FilterScreen(
                navController = navController,
                analytics = analytics
            )
        }

        composable(
            Routes.CONTRACTS
        ) {
            ContractListScreen(
                navController = navController,
                remoteConfig = remoteConfig,
                analytics = analytics
            )
        }

        navigation(
            route = "contract_flow/{contractId}",
            startDestination = Routes.CONTRACT_ACTIONS + "/{contractId}"
        ) {
            composable(Routes.CONTRACT_ACTIONS + "/{contractId}") { entry ->
                val parentEntry = remember(entry) {
                    navController.getBackStackEntry("contract_flow/{contractId}")
                }
                val viewModel: ContractActionsViewModel = hiltViewModel(parentEntry)
                ContractActionsScreen(
                    contractId = entry.arguments?.getString("contractId")!!.toInt(),
                    navController = navController,
                    viewModel = viewModel,
                    analytics = analytics
                )
            }

            composable(Routes.CONTRACT_EMAIL_CHANGE) { entry ->
                val parentEntry = remember(entry) {
                    navController.getBackStackEntry("contract_flow/{contractId}")
                }
                val viewModel: ContractActionsViewModel = hiltViewModel(parentEntry)
                ContractEmailChangeScreen(
                    navController,
                    viewModel,
                    analytics = analytics
                )
            }

            composable(Routes.CONTRACT_PHONE_CHANGE) { entry ->
                val parentEntry = remember(entry) {
                    navController.getBackStackEntry("contract_flow/{contractId}")
                }
                val viewModel: ContractActionsViewModel = hiltViewModel(parentEntry)
                ContractPhoneChangeScreen(
                    navController,
                    viewModel,
                    analytics = analytics
                )
            }

            composable(Routes.CONTRACT_VERIFY) { entry ->
                val parentEntry = remember(entry) {
                    navController.getBackStackEntry("contract_flow/{contractId}")
                }
                val viewModel: ContractActionsViewModel = hiltViewModel(parentEntry)
                ContractVerifyScreen(
                    navController,
                    viewModel,
                    analytics = analytics
                )
            }

            composable(Routes.CONTRACT_SUCCESS) { entry ->
                val parentEntry = remember(entry) {
                    navController.getBackStackEntry("contract_flow/{contractId}")
                }
                val viewModel: ContractActionsViewModel = hiltViewModel(parentEntry)
                ContractActionSuccessScreen(
                    navController,
                    viewModel,
                    analytics = analytics
                )
            }
        }



        composable(
            Routes.BACK
        ) {
            if (
                (navController.currentDestination?.route == Routes.LIST_GAS) ||
                (navController.currentDestination?.route == Routes.LIST_LIGHT)
            ) {
                if (bsCounter > 0) {
                    LaunchedEffect(Unit) {
                        dataStoreViewModel.updateBsCounter(bsCounter - 1)
                        navController.popBackStack()
                        if (navController.currentDestination?.route != Routes.INITIAL)
                            navController.popBackStack()
                    }
                } else {
                    analytics.logEvent ( "OpinionBottomSheet" ) {
                        param("eventType", "View")
                    }
                    showOpinionBS = true
                }
            } else {
                if (navController.currentDestination?.route != Routes.INITIAL) { navController.popBackStack() }
            }
        }
    }
}
