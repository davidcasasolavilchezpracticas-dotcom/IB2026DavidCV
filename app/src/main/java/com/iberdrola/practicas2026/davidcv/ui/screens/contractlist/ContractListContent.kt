package com.iberdrola.practicas2026.davidcv.ui.screens.contractlist

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.model.contract.Contract
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractStatus
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractType
import com.iberdrola.practicas2026.davidcv.ui.base.common.ClickEventManager
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalClickManager
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.common.SafeClickTools.Companion.canExecuteMethod
import com.iberdrola.practicas2026.davidcv.ui.base.composables.contractlist.ContractItem
import com.iberdrola.practicas2026.davidcv.ui.base.screens.EmptyContractsScreen

@Composable
fun ContractListContent(
    modifier: Modifier = Modifier,
    lightContractActive: Boolean,
    gasContractActive: Boolean,
    contracts: List<Contract>,
    onEmptyClick: () -> Unit,
    enabled: Boolean = true,
    onClick: (Int) -> Unit,
) {
    val clickManager = remember { ClickEventManager() }

    CompositionLocalProvider(LocalClickManager provides clickManager) {
        val manager = LocalClickManager.current

        Column(
            modifier = modifier
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.clsTitle),
                modifier = Modifier
                    .padding(
                        horizontal = LocalSpacing.current.xl,
                        vertical = LocalSpacing.current.sm
                    ),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            if (gasContractActive || lightContractActive) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .weight(1f)
                ) {
                    items(contracts) { contract ->
                        if (
                            (contract.type == ContractType.GAS && gasContractActive) ||
                            (contract.type == ContractType.LIGHT && lightContractActive)
                        ) {
                            ContractItem(
                                contract = contract,
                                onClick = {
                                    canExecuteMethod(
                                        manager,
                                    ) { onClick(contract.id) }
                                },
                                enabled = enabled
                            )

                            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                        }
                    }
                }
            } else {
                EmptyContractsScreen(
                    modifier = modifier,
                    onBack = {
                        canExecuteMethod(
                            manager,
                        ) { onEmptyClick() }
                    }
                )
            }
        }
    }
}