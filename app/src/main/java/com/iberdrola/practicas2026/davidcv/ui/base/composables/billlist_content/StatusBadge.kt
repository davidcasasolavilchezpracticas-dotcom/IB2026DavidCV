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
import com.iberdrola.practicas2026.davidcv.domain.model.bill.PaymentStatus
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractStatus
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing

/**
 * StatusBadge
 * Muestra un badge con el estado de la factura
 *
 * @param status Indica el estado de la factura.
 */
@Composable
fun StatusBadge(status: PaymentStatus) {
    val (bgColor, textColor) = when (status) {
        PaymentStatus.PAID -> Color(0xFFD1F2E1) to Color(0xFF006633)
        PaymentStatus.PENDING -> Color(0xFFF9D5D5) to Color(0xFFB03A2E)
        PaymentStatus.TRAMITED -> Color(0xFFE3F2FD) to Color(0xFF1976D2)
        PaymentStatus.CANCELED -> Color(0xFFEEEEEE) to Color(0xFF757575)
        PaymentStatus.FIXED_PAYMENT -> Color(0xFFFFF3E0) to Color(0xFFE65100)
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = status.label,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = LocalSpacing.current.sm, vertical = LocalSpacing.current.xs),
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

/**
 * StatusBadge
 * Muestra un badge con el estado del contrato
 *
 * @param status Indica el estado del contrato.
 */
@Composable
fun StatusBadge(status: ContractStatus) {
    val (bgColor, textColor) = when (status) {
        ContractStatus.ACTIVE -> Color(0xFFD1F2E1) to Color(0xFF006633)
        ContractStatus.INACTIVE -> Color(0xFFEEEEEE) to Color(0xFF757575)
    }

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = status.label,
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(horizontal = LocalSpacing.current.sm, vertical = LocalSpacing.current.xs),
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}
