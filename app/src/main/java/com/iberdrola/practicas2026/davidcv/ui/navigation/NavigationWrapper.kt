package com.iberdrola.practicas2026.davidcv.ui.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.composables.initial.GeneralTopAppBar
import com.iberdrola.practicas2026.davidcv.ui.base.screens.OpinionBottomSheet
import com.iberdrola.practicas2026.davidcv.ui.screens.billfilter.FilterScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.billlist.BillListScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActions
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsViewModel
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractactionsuccess.ContractActionSuccessScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractactivate.ContractActivateScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractactiveinfo.ContractActiveInfoScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractemailchange.ContractEmailChangeScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractphonechange.ContractPhoneChangeScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractverify.ContractVerifyScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.contractlist.ContractListScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.initial.InitialScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.useraccount.EditProfileScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.useraccount.UserAccountScreen
import com.iberdrola.practicas2026.davidcv.ui.theme.EnergyGreen
import java.util.Locale

/**
 * NavigationWrapper
 * Se define el contenedor del grafo de navegación con Scaffold integrado para la TopBar y lógica de navegación centralizada.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationWrapper(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    remoteConfig: FirebaseRemoteConfig,
    analytics: FirebaseAnalytics
) {
    val context = LocalContext.current
    val dataStoreViewModel: DataStoreViewModel = hiltViewModel()
    val bsCounter by dataStoreViewModel.bsCounter.collectAsState()
    var viewSelected by rememberSaveable { mutableStateOf(true) }
    var showOpinionBS by remember { mutableStateOf(false) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    if (showOpinionBS) {
        OpinionBottomSheet(
            onDismiss = {
                showOpinionBS = false
                navController.popBackStack()
            },
            onLaterClick = {
                dataStoreViewModel.updateBsCounter(3)
                showOpinionBS = false
                navController.popBackStack()
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
            val eventName = "From${ originRoute.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }To${ route.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() } }"

            analytics.logEvent(eventName) {
                param("eventType", "Movement")
            }
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            GeneralTopAppBar(
                currentRoute = currentRoute,
                navController = navController,
                handleBackNavigation = {
                    handleBackNavigation(
                        currentRoute = currentRoute,
                        navController = navController,
                        bsCounter = bsCounter,
                        dataStoreViewModel = dataStoreViewModel,
                        analytics = analytics,
                        onShowOpinionBS = {
                            showOpinionBS = true
                        }
                    )
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.INITIAL,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.ACCOUNT_INFO) {
                UserAccountScreen(
                    navController = navController,
                    analytics = analytics,
                    onBack = {
                        handleBackNavigation(
                            currentRoute = currentRoute,
                            navController = navController,
                            bsCounter = bsCounter,
                            dataStoreViewModel = dataStoreViewModel,
                            analytics = analytics,
                            onShowOpinionBS = {
                                showOpinionBS = true
                            }
                        )
                    }
                )
            }

            composable(Routes.ACCOUNT_EDIT) {
                EditProfileScreen(
                    navController = navController,
                    analytics = analytics,
                    onBack = {
                        handleBackNavigation(
                            currentRoute = currentRoute,
                            navController = navController,
                            bsCounter = bsCounter,
                            dataStoreViewModel = dataStoreViewModel,
                            analytics = analytics,
                            onShowOpinionBS = {
                                showOpinionBS = true
                            }
                        )
                    }
                )
            }

            composable(Routes.LIST_LIGHT) {
                BillListScreen(
                    navController = navController,
                    viewSelected = viewSelected,
                    analytics = analytics,
                    remoteConfig = remoteConfig,
                    onBack = {
                        handleBackNavigation(
                            currentRoute = currentRoute,
                            navController = navController,
                            bsCounter = bsCounter,
                            dataStoreViewModel = dataStoreViewModel,
                            analytics = analytics,
                            onShowOpinionBS = {
                                showOpinionBS = true
                            }
                        )
                    },
                    modifier = Modifier
                )
            }

            composable(Routes.LIST_GAS) {
                BillListScreen(
                    navController = navController,
                    viewSelected = !viewSelected,
                    analytics = analytics,
                    remoteConfig = remoteConfig,
                    onBack = {
                        handleBackNavigation(
                            currentRoute = currentRoute,
                            navController = navController,
                            bsCounter = bsCounter,
                            dataStoreViewModel = dataStoreViewModel,
                            analytics = analytics,
                            onShowOpinionBS = {
                                showOpinionBS = true
                            }
                        )
                    },
                    modifier = Modifier
                )
            }

            composable(Routes.INITIAL) {
                InitialScreen(
                    navController = navController,
                    modifier = Modifier,
                    analytics = analytics,
                    remoteConfig = remoteConfig
                )
            }

            composable(Routes.FILTER) {
                FilterScreen(
                    navController = navController,
                    analytics = analytics,
                    onBack = {
                        handleBackNavigation(
                            currentRoute = currentRoute,
                            navController = navController,
                            bsCounter = bsCounter,
                            dataStoreViewModel = dataStoreViewModel,
                            analytics = analytics,
                            onShowOpinionBS = {
                                showOpinionBS = true
                            }
                        )
                    }
                )
            }

            composable(Routes.CONTRACTS) {
                ContractListScreen(
                    navController = navController,
                    remoteConfig = remoteConfig,
                    analytics = analytics,
                    onBack = {
                        handleBackNavigation(
                            currentRoute = currentRoute,
                            navController = navController,
                            bsCounter = bsCounter,
                            dataStoreViewModel = dataStoreViewModel,
                            analytics = analytics,
                            onShowOpinionBS = {
                                showOpinionBS = true
                            }
                        )
                    }
                )
            }

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
                            handleBackNavigation(
                                currentRoute = currentRoute,
                                navController = navController,
                                bsCounter = bsCounter,
                                dataStoreViewModel = dataStoreViewModel,
                                analytics = analytics,
                                onShowOpinionBS = {
                                    showOpinionBS = true
                                }
                            )
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
                        analytics =  analytics
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
    }
}