package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractemailchange

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
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.navigation.auxiliar.rememberDefaultClickHandler
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsViewModel

@Composable
fun ContractEmailChangeScreen(
    onNavigatePopUpTo: (String, String) -> Unit,
    viewModel: ContractActionsViewModel,
    analytics: FirebaseAnalytics,
    onNavigate: (String) -> Unit,
    onBack: (Boolean) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        analytics.logEvent ( "ContractEmailChangeScreen" ) {
            param("eventType", "View")
        }
    }

    val safeClick = rememberDefaultClickHandler(
        onClick = {
            onBack(false)
        }
    )


    Box(modifier = Modifier.fillMaxSize()) {
        ContractEmailChangeContent(
            state = state,
            onEmailChanged = viewModel::onEmailChanged,
            onClose = {
                onNavigatePopUpTo(Routes.CONTRACTS, Routes.CONTRACTS)
                Toast.makeText(context, R.string.cascTitleModifyCancel, Toast.LENGTH_SHORT).show()
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
                Toast.makeText(context, R.string.cascTitleModifyCancel, Toast.LENGTH_SHORT).show()
                analytics.logEvent ( "ButtonBack" ) {
                    param("eventType", "Click")
                }
            }
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
