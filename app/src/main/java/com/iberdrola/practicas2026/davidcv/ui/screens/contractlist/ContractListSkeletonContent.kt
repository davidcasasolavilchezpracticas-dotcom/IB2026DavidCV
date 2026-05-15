package com.iberdrola.practicas2026.davidcv.ui.screens.contractlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.composables.contractlist.ContractItemSkeleton

@Composable
fun ContractListSkeletonContent(
    modifier: Modifier = Modifier
){
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

        repeat(2) {
            ContractItemSkeleton(modifier = modifier.padding(horizontal = LocalSpacing.current.md))
        }
    }
}