package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractverify

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessAlarm
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.composables.contractactivate.ContractNavigateButtons
import com.iberdrola.practicas2026.davidcv.ui.base.composables.contractactivate.ContractTopAppBar
import com.iberdrola.practicas2026.davidcv.ui.base.composables.contractverify.ResendCodeInfoBox
import com.iberdrola.practicas2026.davidcv.ui.base.composables.contractverify.SuccessBanner
import com.iberdrola.practicas2026.davidcv.ui.base.screens.AlertDialogOK
import com.iberdrola.practicas2026.davidcv.ui.navigation.DataStoreViewModel
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActions
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsState
import kotlinx.coroutines.delay
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContractVerifyContent(
    dataStoreViewModel: DataStoreViewModel,
    state: ContractActionsState,
    events: ContractVerifyEvents,
) {
    var resendVerificationCode by remember { mutableStateOf(false) }
    var successBanner by remember { mutableStateOf(false) }
    var showTimeLeftAlertDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    var trys = dataStoreViewModel.trys.collectAsState()


    Scaffold(
        topBar = {
            ContractTopAppBar(
                title = when (state.action) {
                    ContractActions.MODIFYEMAIL -> R.string.cvcTitleModifyEmail
                    ContractActions.MODIFYSTATUS -> R.string.cvcTitleDesactivate
                    ContractActions.MODIFYSTATUSEMAIL -> R.string.cvcTitleActivate
                    ContractActions.MODIFYPHONE -> R.string.cvcTitleModifyPhone
                },
                progress = 0.75f,
                onClose = events.onClose
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                ContractNavigateButtons(
                    enable = state.canSubmitVerify,
                    onBack = events.onBack,
                    onNext = events.onNext,
                )
            }
        }

    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(LocalSpacing.current.la)
        ) {
            Text(
                stringResource(R.string.cvcSubtitleInsertCode),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = stringResource(R.string.cvcTextVerifyIdentity) + events.phoneCensurator(state.contract?.phone ?: "123456789") + stringResource(R.string.cvcTextVerifyIdentityEnd),
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(32.dp))

            TextField(
                value = state.verifyCodeTry,
                onValueChange = { events.onVerifyCodeChanged(it) },
                label = {
                    Text(
                        text = stringResource(R.string.cvcTextFieldVerifyCode),
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.DarkGray,
                    unfocusedIndicatorColor = Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            ResendCodeInfoBox(
                trys = trys.value,
                resendCode = resendVerificationCode,
                onResendClick = {
                    if (trys.value > 0){
                        resendVerificationCode = true
                        successBanner = true

                        events.generateNewCode(context)
                        dataStoreViewModel.updateTrys(trys.value - 1)
                    } else {
                        resendVerificationCode = true
                        showTimeLeftAlertDialog = trys.value <= 0
                    }
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            if (successBanner) {
                events.onLoadEnd()
                SuccessBanner(
                    onDismiss = { successBanner = false },
                )
            }

            if (showTimeLeftAlertDialog) {
                events.createText(events.getTimeLeft(context), stringResource(R.string.cvcTextTimeLeftStart), stringResource(R.string.cvcTextTimeLeftEnd))
                AlertDialogOK(
                    icon = Icons.Default.AccessAlarm,
                    titulo = stringResource(R.string.cvcTitleTimeLeft),
                    text = state.timeLeftToResend,
                    confirmText = stringResource(R.string.Ok),
                    onDismiss = { showTimeLeftAlertDialog = false }
                )
            }
        }
    }
}
