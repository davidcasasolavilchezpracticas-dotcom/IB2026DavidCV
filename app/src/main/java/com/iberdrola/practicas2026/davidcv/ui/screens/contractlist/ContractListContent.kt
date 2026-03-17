package com.iberdrola.practicas2026.davidcv.ui.screens.contractlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.iberdrola.practicas2026.davidcv.domain.model.contract.Contract
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.composables.contractlist.ContractItem

@Composable
fun ContractListContent(
    contracts: List<Contract>,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = LocalSpacing.current.lg)
    ) {
        LazyColumn(modifier = modifier.weight(1f)) {
            items(contracts) { contract ->

                ContractItem(
                    contract = contract,
                    onClick = onClick
                )
            }
        }
    }
}