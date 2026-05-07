package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractverify

import android.content.Context
import androidx.compose.runtime.Composable

data class ContractVerifyEvents(
    val createText: (Boolean, String, String) -> Unit,
    val onVerifyCodeChanged: (String) -> Unit,
    val phoneCensurator: (String) -> String,
    val getTimeLeft: (Context) -> Boolean,
    val generateNewCode: () -> Unit,
    val onLoadEnd: () -> Unit,
    val onClose: () -> Unit,
    val onBack: () -> Unit,
    val onNext: () -> Unit,
)
