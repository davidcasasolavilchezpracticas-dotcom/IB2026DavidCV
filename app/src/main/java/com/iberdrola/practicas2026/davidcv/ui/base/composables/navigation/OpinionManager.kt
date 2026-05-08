package com.iberdrola.practicas2026.davidcv.ui.base.composables.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.iberdrola.practicas2026.davidcv.ui.base.screens.OpinionBottomSheet
import com.iberdrola.practicas2026.davidcv.ui.base.screens.ThanksForRatingDialog

@Composable
fun OpinionManager(
    showBS: Boolean,
    showThanks: Boolean,
    onDismissBS: () -> Unit,
    onDismissThanks: () -> Unit,
    onLater: () -> Unit,
    onRated: () -> Unit,
    navController: NavController
) {
    if (showBS) {
        OpinionBottomSheet(
            onDismiss = { onDismissBS(); navController.popBackStack() },
            onLaterClick = { onLater(); onDismissBS(); navController.popBackStack() },
            onRatingSelected = { onRated(); onDismissBS() }
        )
    }

    if (showThanks) {
        ThanksForRatingDialog(onDismiss = { onDismissThanks(); navController.popBackStack() })
    }
}