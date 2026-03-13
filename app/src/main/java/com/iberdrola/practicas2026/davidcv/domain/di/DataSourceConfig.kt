package com.iberdrola.practicas2026.davidcv.domain.di

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * DataSourceConfig
 * Objeto que contiene la configuración de la fuente de datos
 *
 */
object DataSourceConfig {
    var useNetwork by mutableStateOf(true)
}
