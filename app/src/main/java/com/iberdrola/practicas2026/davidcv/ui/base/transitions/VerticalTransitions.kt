package com.iberdrola.practicas2026.davidcv.ui.base.transitions

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically

@JvmSuppressWildcards
fun VerticalEnter():  EnterTransition = slideInVertically(
        initialOffsetY = { it },
        animationSpec = tween(1000)
    ) + fadeIn()