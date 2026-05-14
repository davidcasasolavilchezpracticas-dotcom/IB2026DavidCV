package com.iberdrola.practicas2026.davidcv.ui.base.transitions

import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleOut
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.fadeIn

@JvmSuppressWildcards
fun ScaleExit(): ExitTransition = scaleOut(
        transformOrigin = TransformOrigin.Center,
        animationSpec = tween(500),
        targetScale = 1.1f,
    ) + fadeOut(animationSpec = tween(500))

@JvmSuppressWildcards
fun ScalePopExit(): ExitTransition = scaleOut(
        animationSpec = tween(500),
        targetScale = 0.8f,
    ) + fadeOut(animationSpec = tween(500))

@JvmSuppressWildcards
fun ScaleEnter():  EnterTransition = scaleIn(
        transformOrigin = TransformOrigin.Center,
        animationSpec = tween(500),
        initialScale = 0.8f,
    ) + fadeIn(animationSpec = tween(500))

@JvmSuppressWildcards
fun ScalePopEnter():  EnterTransition = scaleIn(
        animationSpec = tween(500),
        initialScale = 1.2f,
    ) + fadeIn(animationSpec = tween(500))