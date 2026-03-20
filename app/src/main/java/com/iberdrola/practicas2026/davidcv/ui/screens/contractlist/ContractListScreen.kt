package com.iberdrola.practicas2026.davidcv.ui.screens.contractlist

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.di.DataSourceConfig
import com.iberdrola.practicas2026.davidcv.domain.exception.ContractException
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.screens.EmptyContractsScreen
import com.iberdrola.practicas2026.davidcv.ui.base.screens.ErrorScreen
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContractListScreen(
    viewModel: ContractListViewModel = hiltViewModel(),
    navController: NavHostController,
    remoteConfig: FirebaseRemoteConfig,
    analytics: FirebaseAnalytics
) {
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
                Log.d("ComprobacionesRemoteConfig", "Updated keys: " + configUpdate.updatedKeys)
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
                        DataSourceConfig.useNetwork = !DataSourceConfig.useNetwork
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
                            analytics.logEvent ( "RefreshContracts" ) {
                                param("eventType", "RelevantMovements")
                            }
                        }
                    )
                } else {
                    ContractListContent(
                        contracts = contracts,
                        modifier = Modifier.padding(padding),
                        onClick = { id ->
                            navController.navigate(Routes.CONTRACT_ACTIONS + "/$id")
                            analytics.logEvent ( "ButtonContractsInfo" ) {
                                param("eventType", "Click")
                            }
                        },
                        gasContractActive = gasContractActive,
                        lightContractActive = lightContractActive
                    )
                }
            }
            is ContractListState.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF006633))
                }
            }
        }
    }
}
