package com.iberdrola.practicas2026.davidcv.ui.base.transitions

import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.fadeIn

@JvmSuppressWildcards
fun HorizontalExit(): ExitTransition = slideOutHorizontally(
        animationSpec = tween(1000),
        targetOffsetX = { -it },
    ) + fadeOut()

@JvmSuppressWildcards
fun HorizontalPopExit(): ExitTransition = slideOutHorizontally(
        animationSpec = tween(1000),
        targetOffsetX = { it },
    ) + fadeOut()


@JvmSuppressWildcards
fun HorizontalEnter():  EnterTransition = slideInHorizontally(
        animationSpec = tween(1000),
        initialOffsetX = { it },
    ) + fadeIn()


@JvmSuppressWildcards
fun HorizontalPopEnter():  EnterTransition = slideInHorizontally(
        animationSpec = tween(1000),
        initialOffsetX = { -it },
    ) + fadeIn()