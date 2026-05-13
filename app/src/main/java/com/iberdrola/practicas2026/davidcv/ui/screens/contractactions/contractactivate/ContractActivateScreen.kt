package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractactivate

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.base.common.ClickEventManager
import com.iberdrola.practicas2026.davidcv.ui.navigation.DataStoreViewModel
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.navigation.auxiliar.rememberDefaultClickHandler
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsViewModel

@Composable
fun ContractActivateScreen(
    viewModelDS: DataStoreViewModel = hiltViewModel(),
    onNavigatePopUpTo: (String, String) -> Unit,
    viewModel: ContractActionsViewModel,
    analytics: FirebaseAnalytics,
    onNavigate: (String) -> Unit,
    manager: ClickEventManager,
    onBack: (Boolean) -> Unit,
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        analytics.logEvent ( "ContractActivateScreen" ) {
            param("eventType", "View")
        }
    }

    BackHandler {
        onBack(false)
        Toast.makeText(context, R.string.cascTitleActivateCancel, Toast.LENGTH_SHORT).show()
    }

    val safeClick = rememberDefaultClickHandler(
        onClick = {
            onBack(false)
        }
    )

    Box(modifier = Modifier.fillMaxSize()) {
        ContractActivateContent(
            state = state,
            manager = manager,
            onCensurator = viewModel::censurator,
            onEmailChanged = viewModel::onEmailChanged,
            onAcceptedChanged = viewModel::onAcceptedChanged,
            onClose = {
                onNavigatePopUpTo(Routes.CONTRACTS, Routes.CONTRACTS)
                Toast.makeText(context, R.string.cascTitleActivateCancel, Toast.LENGTH_SHORT).show()
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
                Toast.makeText(context, R.string.cascTitleActivateCancel, Toast.LENGTH_SHORT).show()
                analytics.logEvent ( "ButtonBack" ) {
                    param("eventType", "Click")
                }
            }
        )
    }
}
