package com.iberdrola.practicas2026.davidcv.ui.screens.useraccount

import com.iberdrola.practicas2026.davidcv.domain.model.account.Account

/**
 * UserAccountState
 * Estado de la pantalla de cuenta de usuario
 *
 * @param isLoading Indica si se está cargando la información
 * @param account Información de la cuenta del usuario
 * @param error Mensaje de error si ocurre algún fallo
 */
data class UserAccountState(
    val isLoading: Boolean = false,
    val account: Account? = null,
    val error: String? = null
)
