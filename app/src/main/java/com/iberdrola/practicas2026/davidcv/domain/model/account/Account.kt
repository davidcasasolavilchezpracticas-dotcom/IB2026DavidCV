package com.iberdrola.practicas2026.davidcv.domain.model.account

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Account
 * Modelo de datos para la cuenta del usuario
 *
 * @param id Identificador único
 * @param name Nombre del usuario
 * @param email Correo electrónico
 * @param profileImage Imagen de perfil
 */
data class Account(
    val id: Int,
    val name: String,
    val email: String,
    var profileImage: Any? = null
)
