package com.iberdrola.practicas2026.davidcv.ui.screens.billlist

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.iberdrola.practicas2026.davidcv.domain.di.DataSourceConfig
import com.iberdrola.practicas2026.davidcv.ui.base.composables.TabItem
import com.iberdrola.practicas2026.davidcv.ui.base.screens.ErrorScreen
import com.iberdrola.practicas2026.davidcv.ui.theme.DividerGray
import kotlinx.coroutines.launch


@Composable
fun BillListScreen(
    viewModel: BillListViewModel = hiltViewModel(),
    navController: NavController,
    modifier: Modifier,
    viewSelected: Boolean = true
) {
    val lightBillsState by viewModel.lightBillsState.collectAsStateWithLifecycle()
    val gasBillsState by viewModel.gasBillsState.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(
        initialPage = if (viewSelected) 0 else 1,
        pageCount = { 2 }
    )
    val coroutineScope = rememberCoroutineScope()

    BackHandler {
        navController.navigate("back")
    }

    LaunchedEffect(Unit) {
        viewModel.getLightBills()
        viewModel.getGasBills()
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
                isSelected = pagerState.currentPage == 0,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                }
            )

            Spacer(modifier = modifier.width(24.dp))

            TabItem(
                text = "Gas",
                isSelected = pagerState.currentPage == 1,
                onClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(1)
                    }
                }
            )
        }

        Divider(
            color = DividerGray,
            thickness = 2.dp,
        )

        Spacer(modifier = modifier.height(24.dp))

        HorizontalPager(
            state = pagerState,
            modifier = modifier.fillMaxSize()
        ) { page ->
            val currentState = if (page == 0) lightBillsState else gasBillsState
            BillListContent(
                state = currentState,
                modifier = modifier,
                onErrorClick = {
                    DataSourceConfig.useNetwork = !DataSourceConfig.useNetwork
                    navController.popBackStack()
                }
            )
        }
    }
}

@Composable
private fun BillListContent(
    state: BillListState,
    modifier: Modifier,
    onErrorClick: () -> Unit
) {
    when (state) {
        is BillListState.Loading -> {
            BillListContentEmpty(modifier = modifier)
        }
        is BillListState.Error -> {
            ErrorScreen(
                message = state.message.message ?: "Error desconocido",
                modifier = modifier,
                onClick = {
                    onErrorClick()
                }
            )
        }
        is BillListState.Success -> {
            val bills = state.bills
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

@Preview
@Composable
fun PreviewBLS(){
    BillListScreen(modifier = Modifier, navController = rememberNavController())
}
