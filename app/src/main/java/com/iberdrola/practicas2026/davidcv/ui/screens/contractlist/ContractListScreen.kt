package com.iberdrola.practicas2026.davidcv.ui.screens.contractlist

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.di.DataSourceConfig
import com.iberdrola.practicas2026.davidcv.domain.exception.ContractException
import com.iberdrola.practicas2026.davidcv.ui.base.common.ClickEventManager
import com.iberdrola.practicas2026.davidcv.ui.base.screens.EmptyContractsScreen
import com.iberdrola.practicas2026.davidcv.ui.base.screens.ErrorScreen
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.theme.EnergyGreen
import com.iberdrola.practicas2026.davidcv.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContractListScreen(
    viewModel: ContractListViewModel = hiltViewModel(),
    remoteConfig: FirebaseRemoteConfig,
    analytics: FirebaseAnalytics,
    onNavigate: (String) -> Unit,
    manager: ClickEventManager,
    onBack: () -> Unit
) {
    BackHandler {
        onBack()
        analytics.logEvent("ButtonBack") {
            param("eventType", "RelevantMovements")
        }
    }

    LaunchedEffect(Unit) {
        analytics.logEvent ( "ContractListScreen" ) {
            param("eventType", "View")
        }
    }

    val state = viewModel.contractsState.collectAsState()

    // Usamos mutableStateOf para que Compose sepa que debe redibujar cuando cambie el valor
    var gasContractActive by remember {
        mutableStateOf(remoteConfig.getBoolean("ContractGasAviable"))
    }

    var lightContractActive by remember {
        mutableStateOf(remoteConfig.getBoolean("ContractLightAviable"))
    }

    // Listener para actualizaciones en tiempo real
    LaunchedEffect(Unit) {
        // Forzamos un fetch al entrar para asegurar datos frescos
        remoteConfig.fetchAndActivate().addOnCompleteListener {
            gasContractActive = remoteConfig.getBoolean("ContractGasAviable")
            lightContractActive = remoteConfig.getBoolean("ContractLightAviable")
        }

        // Suscribirse a cambios en tiempo real (si está configurado en Firebase)
        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate: ConfigUpdate) {
                if (configUpdate.updatedKeys.contains("ContractGasAviable") || configUpdate.updatedKeys.contains("ContractLightAviable")) {
                    remoteConfig.activate().addOnCompleteListener {
                        gasContractActive = remoteConfig.getBoolean("ContractGasAviable")
                        lightContractActive = remoteConfig.getBoolean("ContractLightAviable")
                    }
                }
            }

            override fun onError(error: FirebaseRemoteConfigException) {
                Log.w("ComprobacionesRemoteConfig", "Config update error with code: " + error.code, error)
            }
        })
    }

    when (state.value) {
        is ContractListState.Error -> {
            ErrorScreen(
                isConexionError = (state.value as ContractListState.Error).exception is ContractException.ConexionFailed,
                message = (state.value as ContractListState.Error).exception.message ?: R.string.blcUnknownError.toString(),
                img = if ((state.value as ContractListState.Error).exception is ContractException.ConexionFailed)
                    Icons.Default.WifiOff else Icons.Default.Error,
                modifier = Modifier,
                onClick = {
                    DataSourceConfig.useNetwork = !DataSourceConfig.useNetwork
                    onBack()
                }
            )
        }
        is ContractListState.Success -> {
            val contracts = (state.value as ContractListState.Success).contracts
            if (contracts.isEmpty()) {
                EmptyContractsScreen(
                    modifier = Modifier,
                    onBack = {
                        onNavigate(Routes.INITIAL)
                        analytics.logEvent ( "RefreshContracts" ) {
                            param("eventType", "RelevantMovements")
                        }
                    }
                )
            } else {
                ContractListContent(
                    lightContractActive = lightContractActive,
                    gasContractActive = gasContractActive,
                    contracts = contracts,
                    modifier = Modifier,
                    manager = manager,
                    onClick = { id ->
                        onNavigate(Routes.CONTRACT_ACTIONS + "/$id")
                        analytics.logEvent ( "ButtonContractsInfo" ) {
                            param("eventType", "Click")
                        }
                    },
                    onEmptyClick = {
                        onNavigate(Routes.INITIAL)
                        analytics.logEvent ( "ButtonEmpty" ) {
                            param("eventType", "Click")
                        }
                    },
                )
            }
        }
        is ContractListState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = EnergyGreen)
            }
        }

    }
}
