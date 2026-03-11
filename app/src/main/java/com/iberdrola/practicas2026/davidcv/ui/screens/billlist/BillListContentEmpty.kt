package com.iberdrola.practicas2026.davidcv.ui.screens.billlist

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.ui.theme.SkeletonGreen


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

        // Simular la lista del historial
        repeat(size - 1) {
            FacturaItemSkeleton(modifier)
        }
    }
}

// Esqueleto
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

// Esqueleto item
@Composable
fun FacturaItemSkeleton(modifier: Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Cuadro de estado (izquierda)
            Box(modifier = modifier
                .size(24.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmerEffect())
            Spacer(modifier = modifier.width(12.dp))

            // Textos centrales
            Box(
                modifier =
                    modifier
                        .weight(1f)
                        .height(24.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmerEffect(),
            )

            Spacer(modifier = Modifier.width(24.dp))

            // Icono/Monto (derecha)
            Box(modifier = modifier
                .size(20.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmerEffect())
        }
        HorizontalDivider(thickness = 0.5.dp, color = Color(0xFFEEEEEE))
    }
}

// Efecto
@Composable
fun Modifier.shimmerEffect(): Modifier {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(1200, easing = LinearEasing),
                repeatMode = RepeatMode.Restart,
            ),
        label = "shimmer",
    )

    val shimmerColors =
        listOf(
            SkeletonGreen.copy(alpha = 0.6f),
            SkeletonGreen.copy(alpha = 0.2f),
            SkeletonGreen.copy(alpha = 0.6f),
        )

    val brush =
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnim, y = translateAnim),
        )

    return this.background(brush)
}


@Preview
@Composable
fun PreviewBLCE(){
    BillListContentEmpty(modifier = Modifier)
}