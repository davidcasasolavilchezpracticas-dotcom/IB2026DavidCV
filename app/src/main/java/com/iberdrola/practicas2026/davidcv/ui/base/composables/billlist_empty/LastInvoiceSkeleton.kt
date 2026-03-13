package com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_empty

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.ui.base.composables.modifier_extensions.shimmerEffect

/**
 * LastInvoiceSkeleton
 * Muestra un esqueleto de la última factura
 *
 * @param modifier
 */
@Composable
fun LastInvoiceSkeleton(modifier: Modifier) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
    ) {
        Column(modifier = modifier.padding(16.dp)) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = modifier
                            .size(120.dp, 20.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )
                    Box(
                        modifier = modifier
                            .size(180.dp, 16.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .shimmerEffect()
                    )
                }
                Box(modifier = modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect())
            }
            Spacer(modifier = modifier.height(24.dp))
            Box(
                modifier = modifier
                    .size(100.dp, 32.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
            Spacer(modifier = modifier.height(16.dp))
            HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEEEEEE))
            Spacer(modifier = modifier.height(16.dp))
            Box(
                modifier = modifier
                    .size(60.dp, 18.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .shimmerEffect()
            )
            Spacer(modifier = modifier.height(12.dp))
            Box(
                modifier =
                    modifier
                        .fillMaxWidth()
                        .height(20.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect(),
            )
        }
    }
}