package com.iberdrola.practicas2026.davidcv.ui.base.composables.initial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.theme.White

/**
 * SettingSwitchItem
 * Muestra un ítem de configuración con un texto y un interruptor
 *
 * @param label Texto del ítem
 * @param checked Estado del interruptor
 * @param onCheckedChange Función a ejecutar al cambiar el estado del interruptor
 */
@Composable
fun SettingSwitchItem(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(
            if (checked) 0.65f else 0.8f
        ),
        shape = RoundedCornerShape(16.dp),
        color = Color(0xFF006633).copy(alpha = 0.2f),
    ) {
        Row(
            modifier =
                Modifier
                    .padding(horizontal = LocalSpacing.current.lg, vertical = LocalSpacing.current.sm),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = if (checked) 12.sp else 14.sp
                    ),
                    fontWeight = FontWeight.Medium
                )
            }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors =
                    SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF006633),
                        checkedTrackColor = White.copy(alpha = 0.5f),
                        uncheckedThumbColor = Color.Gray,
                        uncheckedTrackColor = Color.LightGray.copy(alpha = 0.5f),
                    ),
            )
        }
    }
}
