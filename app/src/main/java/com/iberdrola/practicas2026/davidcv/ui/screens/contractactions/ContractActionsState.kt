package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions

import android.os.Parcelable
import android.util.Patterns
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.iberdrola.practicas2026.davidcv.domain.model.contract.Contract
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContractActionsState(
    val verifyCodeTry: String = "",
    val verifyCode: String = "123456",
    val timeLeftToResend: String = "",

    val emailTry: String = "",
    var phoneTry: String = "",
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
        get() = emailTry.isNotEmpty() && emailPattern.matches(emailTry)

    val canSubmitPhone: Boolean
        get() = isPhoneValid

    val isPhoneValid: Boolean
        get() = validatePhone(phoneTry) { phoneTry = it }

    companion object {
        val emailPattern = "^\\w(?!\\.)(?!.*\\.\\.)[A-Za-z0-9._-]+@[A-Za-z\\d]{2,}\\.[A-Za-z]{2,}$".toRegex()

        fun validatePhone(phone: String, modifyFormat: (String) -> Unit): Boolean {
            var isValid: Boolean
            try {
                val phoneUtil = PhoneNumberUtil.getInstance()
                val phoneNumber = phoneUtil.parse(phone, "ES")
                isValid = phoneUtil.isValidNumber(phoneNumber)
                if(isValid) { modifyFormat(phoneUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)) }
            }
            catch (e: Exception) { isValid = false }

            return isValid
        }
    }

}