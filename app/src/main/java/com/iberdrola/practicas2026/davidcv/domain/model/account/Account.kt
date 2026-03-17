package com.iberdrola.practicas2026.davidcv.domain.model.account

/**
 * Account
 * Modelo de datos para la cuenta del usuario
 *
 * @param id Identificador único
 * @param name Nombre del usuario
 * @param email Correo electrónico
 * @param profileImage URL o ruta de la imagen de perfil
 */
data class Account(
    val id: Int,
    val name: String,
    val email: String,
    val profileImage: String? = null
)
