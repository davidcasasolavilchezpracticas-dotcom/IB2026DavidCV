package com.iberdrola.practicas2026.davidcv.ui.screens.billlist

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_empty.BillFilterButtonSkeleton
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_empty.FacturaItemSkeleton
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_empty.LastInvoiceSkeleton
import com.iberdrola.practicas2026.davidcv.ui.base.composables.modifier_extensions.shimmerEffect
import com.iberdrola.practicas2026.davidcv.ui.theme.SkeletonGreen

/**
 * BillListContentEmpty
 * Muestra una estructura de carga (Skeleton) mientras se obtienen los datos de las facturas
 *
 * @param size
 * @param modifier
 */
@Composable
fun BillListContentEmpty(
    size: Int = 4,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(LocalSpacing.current.lg),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        LastInvoiceSkeleton(modifier)

        Spacer(modifier = modifier.height(4.dp))

        BillFilterButtonSkeleton(modifier)

        Spacer(modifier = modifier.height(4.dp))

        repeat(size) {
            FacturaItemSkeleton(modifier)
        }
    }
}

/**
 * PreviewBLCE
 * Vista previa del estado de carga del listado
 */
@Composable
@Preview(showBackground = true)
fun PreviewBLCE(){
    BillListContentEmpty(modifier = Modifier)
}
