package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractphonechange

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.base.common.ClickEventManager
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.navigation.auxiliar.rememberDefaultClickHandler
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsViewModel

@Composable
fun ContractPhoneChangeScreen(
    onNavigatePopUpTo: (String, String) -> Unit,
    viewModel: ContractActionsViewModel,
    analytics: FirebaseAnalytics,
    onNavigate: (String) -> Unit,
    manager: ClickEventManager,
    onBack: (Boolean) -> Unit,
) {
    LaunchedEffect(Unit) {
        analytics.logEvent ( "ContractPhoneChangeScreen" ) {
            param("eventType", "View")
        }
    }

    val context = LocalContext.current

    val safeClick = rememberDefaultClickHandler(
        onClick = {
            onBack(false)
        }
    )

    val state by viewModel.state.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        ContractPhoneChangeContent(
            onPhoneChanged = viewModel::onPhoneChanged,
            manager = manager,
            onClose = {
                onNavigatePopUpTo(Routes.CONTRACTS, Routes.CONTRACTS)
                Toast.makeText(context, R.string.cpcToast, Toast.LENGTH_SHORT).show()
                analytics.logEvent ( "ButtonClose" ) {
                    param("eventType", "Click")
                }
            },
            onNext = {
                onNavigate(Routes.CONTRACT_VERIFY)
                analytics.logEvent ( "ButtonNext" ) {
                    param("eventType", "Click")
                }
            },
            onBack = {
                safeClick()
                Toast.makeText(context, R.string.cpcToast, Toast.LENGTH_SHORT).show()
                analytics.logEvent ( "ButtonBack" ) {
                    param("eventType", "Click")
                }
            },
            state = state,
        )

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}
