package com.iberdrola.practicas2026.davidcv.ui.screens.useraccount

import com.iberdrola.practicas2026.davidcv.domain.model.account.Account

data class UserAccountState(
    val account: Account? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
