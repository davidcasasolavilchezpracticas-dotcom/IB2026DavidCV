package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions

import android.os.Parcelable
import android.util.Patterns
import com.iberdrola.practicas2026.davidcv.domain.model.contract.Contract
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContractActionsState(
    val verifyCodeTry: String = "",
    val verifyCode: String = "123456",

    val emailTry: String = "",
    val isAcceptedPolicy: Boolean = false,

    val contract: Contract? = null,
    val errorMessage: String? = null,

    val isLoading: Boolean = false,
    val emailChanged: Boolean = false,

    var isActivation: Boolean = false
) : Parcelable {
    val canSubmitVerify: Boolean
        get() = verifyCodeTry == verifyCode

    val canSubmitEmail: Boolean
        get() = isEmailValid

    val canSubmitEmailAndPolicy: Boolean
        get() = isEmailValid && isAcceptedPolicy

    val isEmailValid: Boolean
        get() = emailTry.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailTry).matches()

}