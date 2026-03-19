package com.iberdrola.practicas2026.davidcv.domain.model.account

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * AccountOption
 * Modelo de datos para la opción de cuenta
 *
 * @param title Título de la opción
 * @param icon Icono de la opción
 * @param onClick Función a ejecutar al hacer clic en la opción
 * @param isCritical Indica si la opción es crítica
 */
data class AccountOption(
    val title: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
    val isCritical: Boolean = false
)