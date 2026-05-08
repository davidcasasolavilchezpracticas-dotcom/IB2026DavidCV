package com.iberdrola.practicas2026.davidcv.ui.navigation.auxiliar

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.iberdrola.practicas2026.davidcv.ui.base.composables.navigation.BlockingOverlay
import com.iberdrola.practicas2026.davidcv.ui.base.composables.navigation.OpinionManager

@Composable
fun UIOverlayManager(
    navController: NavHostController,
    onOpinionDismiss: () -> Unit,
    onThanksDismiss: () -> Unit,
    showThanksDialog: Boolean,
    showOpinionBS: Boolean,
    isProcessing: Boolean,
    onLater: () -> Unit,
    onRated: () -> Unit,
) {
    OpinionManager(
        showBS = showOpinionBS,
        showThanks = showThanksDialog,
        onDismissBS = onOpinionDismiss,
        onDismissThanks = onThanksDismiss,
        onLater = onLater,
        onRated = onRated,
        navController = navController
    )

    if (isProcessing) {
        BlockingOverlay()
    }
}