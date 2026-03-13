package com.iberdrola.practicas2026.davidcv.ui.screens.billlist

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.Fireplace
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.davidcv.domain.model.Bill
import com.iberdrola.practicas2026.davidcv.domain.model.BillType
import com.iberdrola.practicas2026.davidcv.ui.base.common.cardColors
import com.iberdrola.practicas2026.davidcv.ui.base.common.dfLastBill
import com.iberdrola.practicas2026.davidcv.ui.base.common.dfNormalBill
import com.iberdrola.practicas2026.davidcv.ui.theme.White

@Composable
fun BillListContentInfo(
    modifier: Modifier = Modifier,
    bills: List<Bill>
) {
    var actualYear: Int = 0

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Tarjeta de Última Factura
        LastInvoiceCard(bill = bills[0])

        Spacer(modifier = Modifier.height(24.dp))

        // Histórico
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Histórico de facturas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            OutlinedButton(
                onClick = { /* Filtrar */ },
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Color(0xFF006633))
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = null,
                    tint = Color(0xFF006633),
                    modifier = Modifier.size(18.dp)
                )
                Text(
                    text = "Filtrar",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color(0xFF006633)
                )
            }
        }

        // Lista de historial
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(bills.subList(1, bills.size)) { bill ->
                if (actualYear != bill.endDate.year) {
                    actualYear = bill.endDate.year
                    Text(
                        text = actualYear.toString(),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                } else {
                    Divider(
                        color = Color.LightGray,
                        thickness = 0.5.dp
                    )
                }

                FacturaItem(bill = bill)
            }
        }
    }
}

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

@Composable
fun FacturaItem(bill: Bill) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = {
                    Toast.makeText(
                        context,
                        "Factura no disponible",
                        Toast.LENGTH_SHORT,

                    ).show() }
            )
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = bill.endDate.format(dfNormalBill),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = bill.type.label,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(4.dp))
            StatusBadge(isPaid = bill.paymentStatus)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "${bill.value} €",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
            )
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}

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
