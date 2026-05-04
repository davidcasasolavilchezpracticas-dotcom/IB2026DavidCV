package com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_empty

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.composables.modifier_extensions.shimmerEffect
import com.iberdrola.practicas2026.davidcv.ui.theme.SkeletonGreen

/**
 * LastInvoiceSkeleton
 * Muestra un esqueleto de la última factura
 *
 * @param modifier
 */
@Composable
@Preview(showBackground = true)
fun LastInvoiceSkeleton(modifier: Modifier = Modifier) {
    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, SkeletonGreen),
    ) {
        Column(modifier = modifier.padding(LocalSpacing.current.lg)) {
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

                    Box(
                        modifier = modifier
                            .size(128.dp, 24.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background( color = SkeletonGreen )
                            .shimmerEffect()
                    )

                    Box(
                        modifier = modifier
                            .size(250.dp, 20.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background( color = SkeletonGreen )
                            .shimmerEffect()
                    )
                }

                Box(
                    modifier = modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background( color = SkeletonGreen )
                        .shimmerEffect()
                )
            }

            Spacer(modifier = modifier.height(24.dp))

            Box(
                modifier = modifier
                    .size(160.dp, 36.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background( color = SkeletonGreen )
                    .shimmerEffect()
            )

            Spacer(modifier = modifier.height(16.dp))

            HorizontalDivider(
                thickness = 0.75.dp,
                color = SkeletonGreen
            )

            Spacer(modifier = modifier.height(16.dp))

            Box(
                modifier = modifier
                    .size(60.dp, 24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background( color = SkeletonGreen )
                    .shimmerEffect()
            )

            Spacer(modifier = modifier.height(16.dp))

            Box(
                modifier =
                    modifier
                        .fillMaxWidth()
                        .height(24.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background( color = SkeletonGreen )
                        .shimmerEffect(),
            )
        }
    }
}