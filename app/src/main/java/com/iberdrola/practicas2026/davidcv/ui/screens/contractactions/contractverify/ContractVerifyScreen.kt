package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractverify

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.base.common.ClickEventManager
import com.iberdrola.practicas2026.davidcv.ui.base.screens.LoadingScreen
import com.iberdrola.practicas2026.davidcv.ui.navigation.DataStoreViewModel
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.navigation.auxiliar.rememberDefaultClickHandler
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActions.MODIFYEMAIL
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActions.MODIFYPHONE
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActions.MODIFYSTATUSEMAIL
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ContractVerifyScreen(
    onNavigatePopUpTo: (String, String) -> Unit,
    viewModel: ContractActionsViewModel,
    analytics: FirebaseAnalytics,
    onNavigate: (String) -> Unit,
    manager: ClickEventManager,
    onBack: (Boolean) -> Unit,
) {
    val dataStoreViewModel: DataStoreViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current
    val safeClick = rememberDefaultClickHandler(
        onClick = {
            onBack(false)
        }
    )


    LaunchedEffect(Unit) {
        analytics.logEvent ( "ContractVerifyScreen" ) {
            param("eventType", "View")
        }
    }

    val events = ContractVerifyEvents(
        onVerifyCodeChanged = viewModel::onVerifyCodeChanged,
        generateNewCode = {
            viewModel.generateNewCode()
            analytics.logEvent ( "ButtonNewVerifyCodeGenerated" ) {
                param("eventType", "Click")
            }
        },
        onLoadEnd = viewModel::onLoadEnd,
        onClose = {
            onNavigatePopUpTo(Routes.CONTRACTS, Routes.CONTRACTS)
            Toast.makeText(context, R.string.cpcToast, Toast.LENGTH_SHORT).show()
            analytics.logEvent ( "ButtonClose" ) {
                param("eventType", "Click")
            }
            Toast.makeText(
                context,
                when(state.action) {
                    MODIFYEMAIL -> R.string.cascTitleModifyCancel
                    MODIFYSTATUSEMAIL -> R.string.cascTitleActivateCancel
                    MODIFYPHONE -> R.string.cpcToast
                },
                Toast.LENGTH_SHORT
            ).show()
            analytics.logEvent ( "ButtonClose" ) {
                param("eventType", "Click")
            }
        },
        onBack = {
            safeClick()
            analytics.logEvent ( "ButtonBack" ) {
                param("eventType", "Click")
            }
        },
        onNext = { notificator ->
            if (state.canSubmitVerify){
                when (state.action) {
                    MODIFYEMAIL -> {
                        viewModel.updateContractEmail(state.emailTry)
                    }

                    MODIFYPHONE -> {
                        viewModel.updateContractPhone(state.phoneTry)
                    }

                    MODIFYSTATUSEMAIL -> {
                        viewModel.updateContractEmail(state.emailTry)
                    }
                }

                onNavigate(Routes.CONTRACT_SUCCESS)
                notificator()
            } else {
                Toast.makeText(
                    context,
                    "Código de verificación incorrecto",
                    Toast.LENGTH_LONG
                ).show()
            }

            analytics.logEvent ( "ButtonNext" ) {
                param("eventType", "Click")
            }
        },
        phoneCensurator = viewModel::phoneCensurator,
        getTimeLeft = viewModel::getTimeLeft,
        createText = viewModel::createText
    )

    Box(modifier = Modifier.fillMaxSize()) {
        ContractVerifyContent(
            dataStoreViewModel = dataStoreViewModel,
            manager = manager,
            events = events,
            state = state,
        )
    }

    LoadingScreen(state.isLoading)
}
