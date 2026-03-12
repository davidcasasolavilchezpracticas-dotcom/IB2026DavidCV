package com.iberdrola.practicas2026.davidcv.domain.di

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object DataSourceConfig {
    // Usamos mutableStateOf para que la UI pueda reaccionar al cambio si fuera necesario
    var useNetwork by mutableStateOf(true)
}
