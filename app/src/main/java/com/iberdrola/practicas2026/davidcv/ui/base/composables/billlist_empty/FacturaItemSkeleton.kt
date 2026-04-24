package com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_empty

import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.composables.modifier_extensions.shimmerEffect
import com.iberdrola.practicas2026.davidcv.ui.theme.SkeletonGreen

/**
 * FacturaItemSkeleton
 * Muestra un esqueleto de factura para la lista de facturas
 *
 * @param modifier
 */
@Composable
@Preview(showBackground = true)
fun FacturaItemSkeleton(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = LocalSpacing.current.lg),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth(0.9f),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(color = SkeletonGreen)
                        .shimmerEffect()
                )

                Spacer(modifier = modifier.width(4.dp))

                Box(
                    modifier =
                        modifier
                            .width(200.dp)
                            .height(36.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(color = SkeletonGreen)
                            .shimmerEffect()
                )
            }

            //Spacer(modifier = Modifier.width(125.dp))

            Box(
                modifier = modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .align(Alignment.CenterVertically)
                    .background( color = SkeletonGreen )
                    .shimmerEffect()
            )
        }

        HorizontalDivider(thickness = 0.75.dp, color = SkeletonGreen)
    }
}