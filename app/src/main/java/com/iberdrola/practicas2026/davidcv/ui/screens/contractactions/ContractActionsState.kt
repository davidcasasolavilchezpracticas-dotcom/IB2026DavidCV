package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions

import android.os.Parcelable
import android.util.Patterns
import com.iberdrola.practicas2026.davidcv.domain.model.contract.Contract
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContractActionsState(
    val verifyCodeTry: String = "",
    val verifyCode: String = "123456",
    val timeLeftToResend: String = "",

    val emailTry: String = "",
    val phoneTry: String = "",
    val isAcceptedPolicy: Boolean = false,

    val contract: Contract? = null,
    val errorMessage: String? = null,

    val isLoading: Boolean = false,
    val emailChanged: Boolean = false,
    val phoneChanged: Boolean = false,

    var action: ContractActions = ContractActions.MODIFYEMAIL
) : Parcelable {
    val canSubmitVerify: Boolean
        get() = verifyCodeTry == verifyCode

    val buttonEnabledVerify: Boolean
        get() = verifyCodeTry.length == 6

    val canSubmitEmail: Boolean
        get() = isEmailValid

    val canSubmitEmailAndPolicy: Boolean
        get() = isEmailValid && isAcceptedPolicy

    val isEmailValid: Boolean
        get() = emailTry.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailTry).matches()

    val canSubmitPhone: Boolean
        get() = isPhoneValid

    val isPhoneValid: Boolean
        get() = phoneTry.isNotEmpty() && Patterns.PHONE.matcher(phoneTry).matches()

}