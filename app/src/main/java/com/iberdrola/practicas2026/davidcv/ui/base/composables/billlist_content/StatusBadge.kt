package com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * StatusBadge
 * Muestra un badge con el estado de la factura
 *
 * @param isPaid Indica si la factura está pagada
 */
@Composable
fun StatusBadge(isPaid: Boolean) {
    val bgColor = if (isPaid) Color(0xFFD1F2E1) else Color(0xFFF9D5D5)
    val textColor = if (isPaid) Color(0xFF006633) else Color(0xFFB03A2E)
    val label = if (isPaid) "Pagada" else "Pendiente de Pago"

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}