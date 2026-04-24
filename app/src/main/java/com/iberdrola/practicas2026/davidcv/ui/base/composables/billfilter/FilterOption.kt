package com.iberdrola.practicas2026.davidcv.ui.base.composables.billfilter

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.common.Spacing
import com.iberdrola.practicas2026.davidcv.ui.theme.EnergyGreen

/**
 * Funciones para el composable FilterOption
 *
 * @param label Etiqueta del checkbox
 * @param value Valor del checkbox
 * @param onCheckedChange Callback cuando se cambia el valor del checkbox
 */
@Composable
fun FilterOption(
        label: String,
        value: Boolean,
        onCheckedChange: (Boolean) -> Unit
) {
    val backgroundColor by animateColorAsState(
        if (value) EnergyGreen else Color.Transparent,
        label = "color"
    )

    Row(
        modifier = Modifier
            .clipToBounds()
            .clip(RoundedCornerShape(6.dp))
            .clickable { onCheckedChange(!value) }
            .padding(LocalSpacing.current.sm),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(backgroundColor)
                .border(
                    width = 2.dp,
                    color = EnergyGreen,
                    shape = RoundedCornerShape(6.dp)
                )
                .clickable { onCheckedChange(!value) }
                .padding(2.dp),
            contentAlignment = Alignment.Center
        ) {
            if (value) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}