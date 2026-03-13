package com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Fireplace
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.davidcv.domain.model.Bill
import com.iberdrola.practicas2026.davidcv.domain.model.BillType
import com.iberdrola.practicas2026.davidcv.ui.base.common.cardColors
import com.iberdrola.practicas2026.davidcv.ui.base.common.dfLastBill
import com.iberdrola.practicas2026.davidcv.ui.theme.White

/**
 * LastInvoiceCard
 * Muestra la última factura
 *
 * @param bill Última factura
 */
@Composable
fun LastInvoiceCard(bill: Bill) {
    Card(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFFD1E0DB)),
        colors = cardColors,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(color = White)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Última factura",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = bill.type.label,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Icon(
                    imageVector = if (bill.type == BillType.LIGHT) Icons.Outlined.Lightbulb else Icons.Outlined.Fireplace,
                    contentDescription = null,
                    tint = Color(0xFF006633)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${bill.value} €",
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "${bill.startDate.format(dfLastBill)} - ${bill.endDate.format(dfLastBill)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            StatusBadge(isPaid = bill.paymentStatus)
        }
    }
}