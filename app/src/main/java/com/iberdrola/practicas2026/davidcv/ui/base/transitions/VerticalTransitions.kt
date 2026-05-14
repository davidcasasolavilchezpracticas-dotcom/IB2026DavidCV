package com.iberdrola.practicas2026.davidcv.ui.base.transitions

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically

@JvmSuppressWildcards
fun VerticalEnter():  EnterTransition = slideInVertically(
        initialOffsetY = { it },
        animationSpec = tween(1000)
    ) + fadeIn()

@JvmSuppressWildcards
fun VerticalExit():  ExitTransition = slideOutVertically(
    targetOffsetY = { -it },
    animationSpec = tween(1000)
) + fadeOut()

@JvmSuppressWildcards
fun VerticalPopEnter():  EnterTransition = slideInVertically(
    initialOffsetY = { -it },
    animationSpec = tween(1000)
) + fadeIn()

@JvmSuppressWildcards
fun VerticalPopExit():  ExitTransition = slideOutVertically(
    targetOffsetY = { it },
    animationSpec = tween(1000)
) + fadeOut()