package com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_empty

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.composables.modifier_extensions.shimmerEffect
import com.iberdrola.practicas2026.davidcv.ui.theme.SkeletonGreen


@Composable
@Preview(showBackground = true)
fun BillItemSkeleton(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = LocalSpacing.current.md),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth(0.8f),
                verticalArrangement = Arrangement.Top
            ) {
                Box(
                    modifier = modifier
                        .height(16.dp)
                        .width(160.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )

                Spacer(modifier = modifier.height(4.dp))

                Box(
                    modifier = modifier
                        .height(12.dp)
                        .width(96.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )

                Spacer(modifier = modifier.height(4.dp))

                Box(
                    modifier = modifier
                        .height(16.dp)
                        .width(120.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .shimmerEffect()
                )
            }

            Box(
                modifier = modifier
                    .height(24.dp)
                    .width(64.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .align(Alignment.CenterVertically)
                    .shimmerEffect()
            )

        }

        HorizontalDivider(thickness = 0.75.dp, color = SkeletonGreen)
    }
}