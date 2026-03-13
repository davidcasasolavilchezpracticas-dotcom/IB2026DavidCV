package com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_empty

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.ui.base.composables.modifier_extensions.shimmerEffect

/**
 * FacturaItemSkeleton
 * Muestra un esqueleto de factura para la lista de facturas
 *
 * @param modifier
 */
@Composable
fun FacturaItemSkeleton(modifier: Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = modifier
                .size(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmerEffect())
            Spacer(modifier = modifier.width(12.dp))

            Box(
                modifier =
                    modifier
                        .weight(1f)
                        .height(24.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmerEffect(),
            )

            Spacer(modifier = Modifier.width(24.dp))

            Box(modifier = modifier
                .size(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmerEffect())
        }
        HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEEEEEE))
    }
}