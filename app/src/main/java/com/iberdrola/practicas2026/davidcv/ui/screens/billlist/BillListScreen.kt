package com.iberdrola.practicas2026.davidcv.ui.screens.billlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.iberdrola.practicas2026.davidcv.ui.base.composables.TabItem
import com.iberdrola.practicas2026.davidcv.ui.theme.DividerGray


@Composable
fun BillListScreen(
    viewModel: BillListViewModel = hiltViewModel(),
    modifier: Modifier
) {
    val currentState by viewModel.billsState.collectAsStateWithLifecycle()

    var lightViewSelected by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.getLightBills()
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // Encabezado
        Text(
            text = "Mis facturas",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "C/ PALMA - ARTA KM 49, 5 , 4°A - PINTO - MADRID",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            modifier = modifier.padding(vertical = 8.dp)
        )

        Spacer(modifier = modifier.height(16.dp))

        // Tabs (Luz / Gas)
        Row(
            modifier = modifier
                .zIndex(1f)
                .padding(bottom = 3.dp)
                .fillMaxWidth()
        ) {
            TabItem(
                text = "Luz",
                isSelected = lightViewSelected,
                onClick = {
                    lightViewSelected = true
                    viewModel.getLightBills()
                }
            )

            Spacer(modifier = modifier.width(24.dp))

            TabItem(
                text = "Gas",
                isSelected = !lightViewSelected,
                onClick = {
                    lightViewSelected = false
                    viewModel.getGasBills()
                }
            )

        }

        Divider(
            color = DividerGray,
            thickness = 2.dp,
        )

        Spacer(modifier = modifier.height(24.dp))

        when (currentState) {
            is BillListState.Loading -> {
                BillListContentEmpty(modifier = modifier)
            }
            is BillListState.Error -> {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .background(Color.Red)
                ) {}
            }
            is BillListState.Success -> {
                val bills = (currentState as BillListState.Success).bills
                if (bills.isEmpty()) {
                    Column(
                        modifier = modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No tienes facturas disponibles",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray
                        )
                    }
                } else {
                    BillListContentInfo(
                        bills = bills,
                        modifier = modifier
                    )
                }
            }
        }
    }
}





@Preview
@Composable
fun PreviewBLS(){
    BillListScreen(modifier = Modifier)
}
