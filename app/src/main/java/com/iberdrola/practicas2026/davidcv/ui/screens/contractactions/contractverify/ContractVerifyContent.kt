package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractverify

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.permissions.AppPermissions
import com.iberdrola.practicas2026.davidcv.ui.base.common.ClickEventManager
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalClickManager
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.common.SafeClickTools.Companion.canExecuteMethod
import com.iberdrola.practicas2026.davidcv.ui.base.composables.contractactivate.ContractNavigateButtons
import com.iberdrola.practicas2026.davidcv.ui.base.composables.contractactivate.ContractTopAppBar
import com.iberdrola.practicas2026.davidcv.ui.base.composables.contractverify.ResendCodeInfoBox
import com.iberdrola.practicas2026.davidcv.ui.base.composables.contractverify.VerifyFeedbackSection
import com.iberdrola.practicas2026.davidcv.ui.base.composables.contractverify.VerifyInputFields
import com.iberdrola.practicas2026.davidcv.ui.base.composables.contractverify.VerifyInstructionSection
import com.iberdrola.practicas2026.davidcv.ui.base.composables.contractverify.getActionResources
import com.iberdrola.practicas2026.davidcv.ui.helper.NotificationHandler
import com.iberdrola.practicas2026.davidcv.ui.helper.rememberPermissionsLauncher
import com.iberdrola.practicas2026.davidcv.ui.navigation.DataStoreViewModel
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsState

@SuppressLint("LocalContextGetResourceValueCall")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContractVerifyContent(
    dataStoreViewModel: DataStoreViewModel,
    state: ContractActionsState,
    events: ContractVerifyEvents,
) {
    val context = LocalContext.current
    val trys by dataStoreViewModel.trys.collectAsState()
    val notificationHandler = remember { NotificationHandler(context = context) }

    // Estados locales de UI
    var successBanner by remember { mutableStateOf(false) }
    var showTimeLeftAlertDialog by remember { mutableStateOf(false) }
    var shouldNotifyNewCode by remember { mutableStateOf(false) }

    val (appBarTitle, successNotifyText) = getActionResources(state.action)

    // Launcher para notificación de éxito al finalizar
    val requestSuccessPermission = rememberPermissionsLauncher(
        permissions = listOf(AppPermissions.Notifications),
        onAllGranted = {
            notificationHandler.showSimpleNotification(
                contentTitle = context.getString(R.string.cascSuccessVerify),
                contentText = successNotifyText
            )
        },
        onDenied = {
            Toast.makeText(
                context,
                successNotifyText,
                Toast.LENGTH_LONG
            ).show()
        }
    )

    // Launcher para nuevo código (Garantiza tiempo real)
    val requestNewCodePermission = rememberPermissionsLauncher(
        permissions = listOf(AppPermissions.Notifications),
        onAllGranted = {
            notificationHandler.showSimpleNotification(
                contentTitle = context.getString(R.string.cascNewCode),
                contentText = context.getString(R.string.cascNewCodeText) + state.verifyCode
            )
        },
        onDenied = {
            Toast.makeText(
                context,
                context.getString(R.string.cascNewCodeText) + state.verifyCode,
                Toast.LENGTH_LONG
            ).show()
        }
    )


    // Reacciona al cambio de código para disparar la notificación en tiempo real
    LaunchedEffect(state.verifyCode) {
        if (shouldNotifyNewCode && state.verifyCode.isNotEmpty()) {
            requestNewCodePermission()
            shouldNotifyNewCode = false
        }
    }


    val clickManager = remember { ClickEventManager() }

    CompositionLocalProvider(LocalClickManager provides clickManager) {
        val manager = LocalClickManager.current

        Scaffold(
            topBar = {
                ContractTopAppBar(
                    title = appBarTitle,
                    progress = 0.75f,
                    onClose ={
                        canExecuteMethod(
                            manager,
                        ) { events.onClose() }
                    },
                )
            },
            bottomBar = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ContractNavigateButtons(
                        enable = state.canSubmitVerify,
                        onBack = {
                            canExecuteMethod(
                                manager,
                            ) { events.onBack() }
                        },
                        onNext = {
                            canExecuteMethod(
                                manager,
                            ) {
                                events.onNext()
                                requestSuccessPermission()
                            }
                        },
                    )
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = LocalSpacing.current.la)
                    .verticalScroll(rememberScrollState())
            ) {
                VerifyInstructionSection(state, events)

                VerifyInputFields(state, events)

                ResendCodeInfoBox(
                    trys = trys,
                    resendCode = shouldNotifyNewCode,
                    onResendClick = {
                        canExecuteMethod(
                            manager,
                        ) {
                            if (trys > 0) {
                                successBanner = true
                                shouldNotifyNewCode = true
                                events.generateNewCode()
                                dataStoreViewModel.updateTrys(trys - 1)
                            } else {
                                showTimeLeftAlertDialog = true
                            }
                        }
                    },
                )

                Spacer(modifier = Modifier.weight(1f))

                VerifyFeedbackSection(
                    successBanner = successBanner,
                    showTimeLeftDialog = showTimeLeftAlertDialog,
                    state = state,
                    events = events,
                    onDismissBanner = {
                        canExecuteMethod(
                            manager,
                        ) { successBanner = false }
                    },
                    onDismissDialog = {
                        canExecuteMethod(
                            manager,
                        ) { showTimeLeftAlertDialog = false }
                    }
                )
            }
        }
    }
}
