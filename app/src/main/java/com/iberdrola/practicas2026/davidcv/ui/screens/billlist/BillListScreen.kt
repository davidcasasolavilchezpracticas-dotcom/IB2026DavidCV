package com.iberdrola.practicas2026.davidcv.ui.screens.billlist

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content.TabItem
import com.iberdrola.practicas2026.davidcv.ui.theme.DividerGray
import kotlinx.coroutines.launch

/**
 * BillListScreen
 * Se define la pantalla que muestra el listado de facturas con soporte para deslizamiento entre tipos
 *
 * @param viewModel
 * @param navController
 * @param modifier
 * @param viewSelected
 */
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
                },
                onEmptyClick = {
                    navController.popBackStack()
                    navController.navigateUp()
                }
            )
        }
    }
}

/**
 * PreviewBLS
 * Vista previa de la pantalla de listado de facturas
 */
@Preview
@Composable
fun PreviewBLS(){
    BillListScreen(modifier = Modifier, navController = rememberNavController())
}
