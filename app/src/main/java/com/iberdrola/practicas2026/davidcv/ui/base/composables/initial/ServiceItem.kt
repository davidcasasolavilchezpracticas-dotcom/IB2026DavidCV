package com.iberdrola.practicas2026.davidcv.ui.base.composables.initial

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing

/**
 * ServiceItem
 * Muestra un ítem de servicio con un icono y un texto
 *
 * @param icon Icono del servicio
 * @param label Texto del servicio
 * @param modifier Modificador para personalizar la apariencia del ítem
 * @param onClick Función a ejecutar al hacer clic en el ítem
 */
@Composable
fun ServiceItem(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    OutlinedCard(
        modifier =
            modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable {
                    onClick()
                },
        shape = RoundedCornerShape(16.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(LocalSpacing.current.lg)
                .fillMaxWidth(),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF006633)
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = label,
                fontWeight = FontWeight.Medium
            )
        }
    }
}