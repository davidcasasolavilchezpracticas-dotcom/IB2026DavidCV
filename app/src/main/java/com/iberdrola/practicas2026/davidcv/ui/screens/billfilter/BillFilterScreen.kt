package com.iberdrola.practicas2026.davidcv.ui.screens.billfilter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billfilter.DateSelector
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billfilter.FilterOption
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billfilter.PriceRangeSelector
import com.iberdrola.practicas2026.davidcv.ui.theme.White
import androidx.compose.runtime.collectAsState
import com.iberdrola.practicas2026.davidcv.domain.model.PaymentStatus

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun FilterScreen(
    viewModel: BillViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .background(White)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column {
            Text(text = "Por fecha", fontWeight = FontWeight.Bold)
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                DateSelector("Desde", Modifier.weight(1f), onConfirm = { date -> viewModel.onStartDateSelected(date)}, onValidDate = viewModel::onValidStartDate)
                DateSelector("Hasta", Modifier.weight(1f), onConfirm = { date -> viewModel.onEndDateSelected(date)}, onValidDate = viewModel::onValidEndDate)
            }
        }

        PriceRangeSelector(
            range = viewModel.state.value.priceRange ?: 0f..200000f,
            onSliderChange = { range -> viewModel.onPriceRangeChanged(range) }
        )

        Column {
            Text(text = "Por estado", fontWeight = FontWeight.Bold)

            FilterOption(label = PaymentStatus.PAID.label, value = viewModel.state.collectAsState().value.paymentStatusPaid, onCheckedChange =  viewModel::onStateChangePaid)
            FilterOption(label = PaymentStatus.PENDING.label, value = viewModel.state.collectAsState().value.paymentStatusPending, onCheckedChange =  viewModel::onStateChangePending)
            FilterOption(label = PaymentStatus.TRAMITED.label, value = viewModel.state.collectAsState().value.paymentStatusTramited, onCheckedChange =  viewModel::onStateChangeTramited)
            FilterOption(label = PaymentStatus.CANCELED.label, value = viewModel.state.collectAsState().value.paymentStatusCanceled, onCheckedChange =  viewModel::onStateChangeCanceled)
            FilterOption(label = PaymentStatus.FIXED_PAYMENT.label, value = viewModel.state.collectAsState().value.paymentStatusFixed, onCheckedChange =  viewModel::onStateChangeFixed)
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = { /* Aplicar */ },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E5D4B)),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Aplicar filtros",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }

            TextButton(
                onClick = { /* Borrar */ }
            ) {
                Text(
                    text = "Borrar filtros",
                    color = Color(0xFF2E5D4B),
                    textDecoration = TextDecoration.Underline,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

}