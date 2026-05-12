package com.iberdrola.practicas2026.davidcv.ui.base.composables.initial

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

@Composable
fun ConnectionMenuItem(
    icon: ImageVector,
    textRes: Int,
    suffix: String = "",
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = { Text(text = stringResource(textRes) + suffix) },
        leadingIcon = { Icon(icon, contentDescription = null) },
        onClick = onClick
    )
}