package com.iberdrola.practicas2026.davidcv.ui.base.composables.contractverify

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessAlarm
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.base.screens.AlertDialogOK
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsState
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractverify.ContractVerifyEvents

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun VerifyFeedbackSection(
    showTimeLeftDialog: Boolean,
    state: ContractActionsState,
    events: ContractVerifyEvents,
    onDismissBanner: () -> Unit,
    onDismissDialog: () -> Unit,
    successBanner: Boolean,
) {
    if (successBanner) {
        events.onLoadEnd()
        SuccessBanner(onDismiss = onDismissBanner)
    }

    if (showTimeLeftDialog) {
        val context = LocalContext.current
        LaunchedEffect(Unit) {
            events.createText(events.getTimeLeft(context), context.getString(R.string.cvcTextTimeLeftStart), context.getString(R.string.cvcTextTimeLeftEnd))
        }
        AlertDialogOK(
            icon = Icons.Default.AccessAlarm,
            titulo = stringResource(R.string.cvcTitleTimeLeft),
            text = state.timeLeftToResend,
            confirmText = stringResource(R.string.Ok),
            onDismiss = onDismissDialog
        )
    }
}