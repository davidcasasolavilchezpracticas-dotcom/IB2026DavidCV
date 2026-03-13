package com.iberdrola.practicas2026.davidcv.ui.screens.billlist

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.domain.model.Bill
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content.FacturaItem
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content.LastInvoiceCard


@Composable
fun BillListContentInfo(
    modifier: Modifier = Modifier,
    bills: List<Bill>
) {
    var actualYear: Int = 0
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        LastInvoiceCard(bill = bills[0])

        Spacer(modifier = Modifier.height(24.dp))

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
                onClick = { /*TODO  Filtrar */ },
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

                FacturaItem(
                    bill = bill,
                    onClick = {
                        Toast.makeText(
                            context,
                            "Factura no disponible",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }
}


