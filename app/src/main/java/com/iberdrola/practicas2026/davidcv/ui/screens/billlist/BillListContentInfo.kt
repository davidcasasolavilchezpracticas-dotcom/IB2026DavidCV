package com.iberdrola.practicas2026.davidcv.ui.screens.billlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.model.bill.Bill
import com.iberdrola.practicas2026.davidcv.domain.model.bill.BillType
import com.iberdrola.practicas2026.davidcv.domain.model.bill.PaymentStatus
import com.iberdrola.practicas2026.davidcv.ui.base.common.ClickEventManager
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalClickManager
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.common.SafeClickTools.Companion.canExecuteMethod
import com.iberdrola.practicas2026.davidcv.ui.base.common.SafeClickTools.Companion.canExecuteMethodListString
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content.ButtonFilter
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content.FacturaItem
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content.LastInvoiceCard
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content.ShowAlertDialogs
import com.iberdrola.practicas2026.davidcv.ui.theme.IB2026DavidCVTheme
import com.iberdrola.practicas2026.davidcv.ui.theme.White
import java.time.LocalDateTime


@Composable
fun BillListContentInfo(
    getSelectedFilters: () -> List<String>,
    modifier: Modifier = Modifier,
    onDeleteFilters: () -> Unit,
    manager: ClickEventManager,
    onFilterClick: () -> Unit,
    enabled: Boolean = true,
    filtersCounter: Int,
    bills: List<Bill>,
) {
    var alertDialogActive by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) { listState.scrollToItem(0) }

    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxWidth()
    ) {

        item {
            Column(
                modifier = Modifier
                    .background(Color.White)
            ) {
                LastInvoiceCard(bill = bills[0])

                Spacer(modifier = Modifier.height(24.dp))

                ShowAlertDialogs(
                    alertDialogActive = alertDialogActive,
                    desactiveAlertDialog = { alertDialogActive = false },
                    showDeleteDialog = showDeleteDialog,
                    desactiveDeleteDialog = {  showDeleteDialog = false },
                    onDeleteFilters = {
                        canExecuteMethod(
                            manager,
                        ) { onDeleteFilters() }
                    },
                    getSelectedFilters = getSelectedFilters,
                    count = filtersCounter
                )
            }
        }

        stickyHeader {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(White)
                    .padding(horizontal = LocalSpacing.current.lg),
            ) {
                Text(
                    text = stringResource(R.string.blciTitle),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                ButtonFilter(
                    onFilterClick = {
                        canExecuteMethod(
                            manager,
                        ) { onFilterClick() }
                    },
                    onLongClick = {
                        canExecuteMethod(
                            manager,
                        ) { if (getSelectedFilters().isNotEmpty()) showDeleteDialog = true }
                    },
                    label = stringResource(R.string.blciButtonFilter),
                    icon = Icons.Default.Tune,
                    selectedFilters = getSelectedFilters(),
                    filtersCount = filtersCounter,
                    enabled = enabled
                )
            }
        }


        val groupedBills = bills.groupBy { it.emisionDate.year }

        groupedBills.forEach { (year, billsInYear) ->
            item {
                Text(
                    text = year.toString(),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(LocalSpacing.current.lg)
                )
            }

            itemsIndexed(billsInYear) { index, bill ->
                FacturaItem(
                    bill = bill,
                    onClick = {
                        canExecuteMethod(
                            manager,
                        ) {  alertDialogActive = true }
                    },
                    enabled = enabled
                )

                if (index < billsInYear.lastIndex) {
                    HorizontalDivider(
                        color = Color.LightGray,
                        thickness = 0.75.dp,
                        modifier = Modifier.padding(horizontal = LocalSpacing.current.lg)
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun BillListContentInfoPreview() {
    val sampleBills = listOf(
        Bill(1, 54.32f, BillType.LIGHT, LocalDateTime.of(2024, 5, 10, 0, 0), LocalDateTime.of(2024, 5, 10, 0, 0), LocalDateTime.of(2024, 5, 10, 0, 0), PaymentStatus.PAID),
        Bill(2, 25.10f, BillType.GAS, LocalDateTime.of(2024, 5, 10, 0, 0), LocalDateTime.of(2024, 4, 5, 0, 0), LocalDateTime.of(2024, 4, 5, 0, 0), PaymentStatus.PENDING),
        Bill(3, 60.00f, BillType.LIGHT, LocalDateTime.of(2024, 5, 10, 0, 0), LocalDateTime.of(2023, 12, 15, 0, 0), LocalDateTime.of(2023, 12, 15, 0, 0), PaymentStatus.PAID),
        Bill(4, 45.00f, BillType.LIGHT, LocalDateTime.of(2024, 5, 10, 0, 0), LocalDateTime.of(2023, 11, 20, 0, 0), LocalDateTime.of(2023, 11, 20, 0, 0), PaymentStatus.PAID)
    )
    IB2026DavidCVTheme {
        BillListContentInfo(
            filtersCounter = 0,
            onFilterClick = {},
            bills = sampleBills,
            onDeleteFilters = {},
            manager = ClickEventManager(),
            getSelectedFilters = { listOf() },
        )
    }
}