package com.iberdrola.practicas2026.davidcv.domain.model.contract

import android.os.Parcelable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Fireplace
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
enum class ContractType(val label: String) : Parcelable {
    LIGHT(label = "Contrato de luz"),
    GAS(label = "Contrato de gas");

    @IgnoredOnParcel
    val icon: ImageVector
        get() = when (this) {
            LIGHT -> Icons.Outlined.Lightbulb
            GAS -> Icons.Outlined.Fireplace
        }
}
