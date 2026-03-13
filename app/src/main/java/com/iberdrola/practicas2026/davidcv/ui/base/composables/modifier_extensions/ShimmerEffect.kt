package com.iberdrola.practicas2026.davidcv.ui.base.composables.modifier_extensions

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.iberdrola.practicas2026.davidcv.ui.theme.SkeletonGreen

/**
 * Modifier.shimmerEffect
 * Crea el efecto de carga en el esqueleto
 */
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