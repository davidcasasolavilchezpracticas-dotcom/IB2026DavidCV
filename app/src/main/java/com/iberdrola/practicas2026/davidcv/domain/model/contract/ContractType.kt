package com.iberdrola.practicas2026.davidcv.domain.model.contract

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Fireplace
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.ui.graphics.vector.ImageVector

enum class ContractType (val label: String, val icon: ImageVector) {
    LIGHT(label = "Contrato de luz", icon = Icons.Outlined.Lightbulb),
    GAS(label = "Contrato de gas", icon = Icons.Outlined.Fireplace)
}