package com.iberdrola.practicas2026.davidcv.ui.base.composables.contractverify

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActions
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActions.MODIFYEMAIL
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActions.MODIFYPHONE
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActions.MODIFYSTATUSEMAIL

@Composable
fun getActionResources(action: ContractActions): Pair<Int, String> {
    val titleRes = when (action) {
        MODIFYEMAIL -> R.string.cvcTitleModifyEmail
        MODIFYSTATUSEMAIL -> R.string.cvcTitleActivate
        MODIFYPHONE -> R.string.cvcTitleModifyPhone
    }
    val notifyText = stringResource(when (action) {
        MODIFYEMAIL -> R.string.cascTitleModify
        MODIFYSTATUSEMAIL -> R.string.cascTitleActivate
        MODIFYPHONE -> R.string.cascTitleModifyPhone
    })
    return titleRes to notifyText
}