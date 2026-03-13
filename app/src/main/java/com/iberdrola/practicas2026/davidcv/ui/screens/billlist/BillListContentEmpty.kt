package com.iberdrola.practicas2026.davidcv.ui.screens.billlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_empty.FacturaItemSkeleton
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_empty.LastInvoiceSkeleton

/**
 * BillListContentEmpty
 * Muestra una estructura de carga (Skeleton) mientras se obtienen los datos de las facturas
 *
 * @param size
 * @param modifier
 */
@Composable
fun BillListContentEmpty(
    size: Int = 3,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        LastInvoiceSkeleton(modifier)

        Spacer(modifier = modifier.height(20.dp))

        repeat(size - 1) {
            FacturaItemSkeleton(modifier)
        }
    }
}

/**
 * PreviewBLCE
 * Vista previa del estado de carga del listado
 */
@Preview
@Composable
fun PreviewBLCE(){
    BillListContentEmpty(modifier = Modifier)
}
