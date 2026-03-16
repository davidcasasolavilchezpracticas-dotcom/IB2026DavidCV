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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.iberdrola.practicas2026.davidcv.domain.model.PaymentStatus
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billfilter.DateSelector
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billfilter.FilterOption
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billfilter.PriceRangeSelector
import com.iberdrola.practicas2026.davidcv.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    navController: NavController,
    viewModel: BillViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

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
                DateSelector(
                    label = "Desde",
                    date = state.startDate,
                    modifier = Modifier.weight(1f),
                    onConfirm = { date -> viewModel.onStartDateSelected(date) },
                    onValidDate = viewModel::onValidStartDate
                )
                DateSelector(
                    label = "Hasta",
                    date = state.endDate,
                    modifier = Modifier.weight(1f),
                    onConfirm = { date -> viewModel.onEndDateSelected(date) },
                    onValidDate = viewModel::onValidEndDate
                )
            }
        }

        PriceRangeSelector(
            selectedRange = state.priceRange ?: 0f..1000f,
            totalRange = 0f..1000f,
            onSliderChange = { range -> viewModel.onPriceRangeChanged(range) }
        )

        Column {
            Text(text = "Por estado", fontWeight = FontWeight.Bold)

            FilterOption(
                label = PaymentStatus.PAID.label,
                value = state.paymentStatusPaid,
                onCheckedChange = viewModel::onStateChangePaid
            )
            FilterOption(
                label = PaymentStatus.PENDING.label,
                value = state.paymentStatusPending,
                onCheckedChange = viewModel::onStateChangePending
            )
            FilterOption(
                label = PaymentStatus.TRAMITED.label,
                value = state.paymentStatusTramited,
                onCheckedChange = viewModel::onStateChangeTramited
            )
            FilterOption(
                label = PaymentStatus.CANCELED.label,
                value = state.paymentStatusCanceled,
                onCheckedChange = viewModel::onStateChangeCanceled
            )
            FilterOption(
                label = PaymentStatus.FIXED_PAYMENT.label,
                value = state.paymentStatusFixed,
                onCheckedChange = viewModel::onStateChangeFixed
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = {
                    // Pasamos el resultado de vuelta a la pantalla anterior
                    navController.previousBackStackEntry?.savedStateHandle?.set("filters_result", state)
                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
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
                onClick = { viewModel.deleteFilters() }
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

@Preview
@Composable
fun PreviewFilterScreen() {
    FilterScreen(navController = rememberNavController())
}
