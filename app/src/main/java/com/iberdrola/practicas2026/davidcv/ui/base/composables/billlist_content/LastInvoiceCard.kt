package com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Fireplace
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstrainScope
import androidx.constraintlayout.compose.ConstraintLayout
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.model.bill.Bill
import com.iberdrola.practicas2026.davidcv.domain.model.bill.BillType
import com.iberdrola.practicas2026.davidcv.domain.model.bill.PaymentStatus
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.common.cardColors
import com.iberdrola.practicas2026.davidcv.ui.base.common.dfLastBill
import com.iberdrola.practicas2026.davidcv.ui.theme.IB2026DavidCVTheme
import com.iberdrola.practicas2026.davidcv.ui.theme.White
import java.time.LocalDateTime

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
        border = BorderStroke(2.dp, Color(0xFF74978D)),
        colors = cardColors,
        modifier = Modifier.fillMaxWidth()
    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxWidth(0.95f)
        ){
            val startGuide = createGuidelineFromStart(0.075f)
            val endGuide = createGuidelineFromEnd(0f)
            val topGuide = createGuidelineFromTop (0.1f)
            val bottomGuide = createGuidelineFromBottom(0.05f)

            val (icon, title, subtitle, price, euro, date, divider, status) = createRefs()

            Text(
                text = stringResource(R.string.LastInvoiceCard),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(title) {
                    start.linkTo(startGuide)
                    top.linkTo(topGuide)
                }
            )

            Text(
                text = bill.type.label,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.constrainAs(subtitle) {
                    start.linkTo(startGuide)
                    top.linkTo(title.bottom, margin = 8.dp)
                }
            )

            Icon(
                imageVector = if (bill.type == BillType.LIGHT) Icons.Outlined.Lightbulb else Icons.Outlined.Fireplace,
                contentDescription = null,
                tint = Color(0xFF006633),
                modifier = Modifier
                    .size(40.dp)
                    .constrainAs(icon) {
                        end.linkTo(endGuide)
                        top.linkTo(topGuide)
                    }
            )

            Text(
                text = "%.2f".format(bill.value),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.constrainAs(price) {
                    start.linkTo(startGuide)
                    top.linkTo(subtitle.bottom, margin = 24.dp)
                }
            )

            Text(
                text = "€",
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 24.sp),
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.constrainAs(euro) {
                    start.linkTo(price.end, margin = 4.dp)
                    bottom.linkTo(price.bottom, margin = 4.dp)
                }
            )

            Text(
                text = "${bill.startDate.format(dfLastBill)} - ${bill.endDate.format(dfLastBill)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.constrainAs(date) {
                    start.linkTo(startGuide)
                    top.linkTo(price.bottom, margin = 8.dp)
                }
            )

            HorizontalDivider(
                thickness = 1.5.dp,
                color = Color(0xFF74978D).copy(alpha = 0.5f),
                modifier = Modifier
                    .fillMaxWidth(0.925f)
                    .constrainAs(divider) {
                        start.linkTo(startGuide, margin = 16.dp)
                        top.linkTo(date.bottom, margin = 16.dp)
                        end.linkTo(endGuide, margin = 16.dp)
                    }
            )

            StatusBadge(
                status = bill.paymentStatus,
                modifier = Modifier.constrainAs(status) {
                    start.linkTo(startGuide)
                    top.linkTo(divider.bottom, margin = 16.dp)
                    bottom.linkTo(bottomGuide, margin = 12.dp)
                }
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LastInvoiceCardPreview() {
    IB2026DavidCVTheme {
        LastInvoiceCard(
            bill = Bill(
                id = 1,
                type = BillType.LIGHT,
                value = 20.00f,
                startDate = LocalDateTime.of(2024, 2, 1, 0, 0),
                endDate = LocalDateTime.of(2024, 3, 4, 0, 0),
                paymentStatus = PaymentStatus.PENDING
            )
        )
    }
}
