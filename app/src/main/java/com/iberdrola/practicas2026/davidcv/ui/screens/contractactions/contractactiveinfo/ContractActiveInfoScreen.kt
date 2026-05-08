package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractactiveinfo

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.iberdrola.practicas2026.davidcv.ui.base.screens.ErrorScreen
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActions
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsViewModel

@Composable
fun ContractActiveInfoScreen(
    viewModel: ContractActionsViewModel,
    analytics: FirebaseAnalytics,
    onNavigate: (String) -> Unit,
    onBack: (Boolean) -> Unit,
) {
    val state by viewModel.state.collectAsState()

    Log.d("Comprobaciones", "Contract -> ${state.contract}")

    LaunchedEffect(Unit) {
        analytics.logEvent ( "ContractActiveInfoScreen" ) {
            param("eventType", "View")
        }
    }

    BackHandler{
        onBack(false)
    }

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
                    onClick = {
                        onBack(false)
                        analytics.logEvent ( "ButtonBack" ) {
                            param("eventType", "Click")
                        }
                    }
                )
            }
            state.contract != null -> {
                ContractActiveInfoContent(
                    contract = state.contract!!,

                    onModifyEmail = {
                        onNavigate(Routes.CONTRACT_EMAIL_CHANGE)
                        state.action = ContractActions.MODIFYEMAIL
                        analytics.logEvent ( "ButtonModifyEmail" ) {
                            param("eventType", "Click")
                        }
                    },
                    onModifyPhone = {
                        onNavigate(Routes.CONTRACT_PHONE_CHANGE)
                        state.action = ContractActions.MODIFYPHONE
                        analytics.logEvent ( "ButtonModifyPhone" ) {
                            param("eventType", "Click")
                        }
                    },
                )
            }
            else -> {
                ErrorScreen(
                    message = state.errorMessage ?: "Unknown error",
                    img = Icons.Default.ErrorOutline,
                    onClick = {
                        onBack(false)
                        analytics.logEvent ( "ButtonBack" ) {
                            param("eventType", "Click")
                        }
                    }
                )
            }
        }
    }
}
