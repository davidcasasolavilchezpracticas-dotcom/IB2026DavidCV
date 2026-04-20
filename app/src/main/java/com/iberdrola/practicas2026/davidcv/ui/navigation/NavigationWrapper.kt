package com.iberdrola.practicas2026.davidcv.ui.navigation

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

    // Lógica centralizada para manejar la navegación hacia atrás
    val handleBackNavigation: () -> Unit = {
        if (currentRoute == Routes.LIST_GAS || currentRoute == Routes.LIST_LIGHT) {
            if (bsCounter > 0) {
                dataStoreViewModel.updateBsCounter(bsCounter - 1)
                navController.popBackStack()
            } else {
                analytics.logEvent("OpinionBottomSheet") {
                    param("eventType", "View")
                }
                showOpinionBS = true
            }
        } else {
            if (currentRoute != Routes.INITIAL && currentRoute != null) {
                navController.popBackStack()
            }
        }
    }

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
            if (currentRoute != Routes.INITIAL && currentRoute != null) {
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { handleBackNavigation() }
                        .padding(LocalSpacing.current.md)
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = null,
                        tint = EnergyGreen
                    )
                    Text(
                        text = stringResource(R.string.matbTitle),
                        color = EnergyGreen,
                        modifier = Modifier
                            .drawBehind {
                                val strokeWidth = 1.dp.toPx()
                                val y = size.height + (-4).dp.toPx()
                                drawLine(
                                    color = EnergyGreen,
                                    start = Offset(0f, y),
                                    end = Offset(size.width, y),
                                    strokeWidth = strokeWidth,
                                )
                            }
                    )
                }
            }
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
                    onBack = handleBackNavigation
                )
            }

            composable(Routes.ACCOUNT_EDIT) {
                EditProfileScreen(
                    navController = navController,
                    analytics = analytics,
                    onBack = handleBackNavigation
                )
            }

            composable(Routes.LIST_LIGHT) {
                BillListScreen(
                    navController = navController,
                    viewSelected = viewSelected,
                    analytics = analytics,
                    remoteConfig = remoteConfig,
                    onBack = handleBackNavigation,
                    modifier = Modifier
                )
            }

            composable(Routes.LIST_GAS) {
                BillListScreen(
                    navController = navController,
                    viewSelected = !viewSelected,
                    analytics = analytics,
                    remoteConfig = remoteConfig,
                    onBack = handleBackNavigation,
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
                    onBack = handleBackNavigation
                )
            }

            composable(Routes.CONTRACTS) {
                ContractListScreen(
                    navController = navController,
                    remoteConfig = remoteConfig,
                    analytics = analytics,
                    onBack = handleBackNavigation
                )
            }

            navigation(
                route = "contract_flow/{contractId}",
                startDestination = Routes.CONTRACT_ACTIONS + "/{contractId}"
            ) {
                composable(Routes.CONTRACT_ACTIONS + "/{contractId}") { entry ->
                    val parentEntry = remember(entry) {
                        try { navController.getBackStackEntry("contract_flow/{contractId}") }
                        catch (e: Exception) { entry }
                    }
                    val viewModel: ContractActionsViewModel = hiltViewModel(parentEntry)
                    ContractActionsScreen(
                        contractId = entry.arguments?.getString("contractId")!!.toInt(),
                        navController = navController,
                        viewModel = viewModel,
                        analytics = analytics,
                        onBack = handleBackNavigation
                    )
                }

                composable(Routes.CONTRACT_EMAIL_CHANGE) { entry ->
                    val parentEntry = remember(entry) {
                        try { navController.getBackStackEntry("contract_flow/{contractId}") }
                        catch (e: Exception) { entry }
                    }
                    val viewModel: ContractActionsViewModel = hiltViewModel(parentEntry)
                    ContractEmailChangeScreen(
                        navController = navController,
                        viewModel = viewModel,
                        analytics = analytics,
                    )
                }

                composable(Routes.CONTRACT_PHONE_CHANGE) { entry ->
                    val parentEntry = remember(entry) {
                        try { navController.getBackStackEntry("contract_flow/{contractId}") }
                        catch (e: Exception) { entry }
                    }
                    val viewModel: ContractActionsViewModel = hiltViewModel(parentEntry)
                    ContractPhoneChangeScreen(
                        navController = navController,
                        viewModel = viewModel,
                        analytics = analytics,
                    )
                }

                composable(Routes.CONTRACT_VERIFY) { entry ->
                    val parentEntry = remember(entry) {
                        try { navController.getBackStackEntry("contract_flow/{contractId}") }
                        catch (e: Exception) { entry }
                    }
                    val viewModel: ContractActionsViewModel = hiltViewModel(parentEntry)
                    ContractVerifyScreen(
                        navController = navController,
                        viewModel = viewModel,
                        analytics = analytics,
                    )
                }

                composable(Routes.CONTRACT_SUCCESS) { entry ->
                    val parentEntry = remember(entry) {
                        try { navController.getBackStackEntry("contract_flow/{contractId}") }
                        catch (e: Exception) { entry }
                    }
                    val viewModel: ContractActionsViewModel = hiltViewModel(parentEntry)
                    ContractActionSuccessScreen(
                        navController = navController,
                        viewModel = viewModel,
                        analytics = analytics,
                    )
                }
            }
        }
    }
}
