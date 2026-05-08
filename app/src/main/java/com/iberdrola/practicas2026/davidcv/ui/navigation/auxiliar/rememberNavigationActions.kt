package com.iberdrola.practicas2026.davidcv.ui.navigation.auxiliar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController

@Composable
fun rememberNavigationActions(
    navController: NavHostController,
    isProcessing: Boolean,
    onProcessingChange: (Boolean) -> Unit
): NavigationActions {
    return remember(navController, isProcessing) {
        NavigationActions(navController, isProcessing, onProcessingChange)
    }
}

class NavigationActions(
    private val navController: NavHostController,
    private val isProcessing: Boolean,
    private val onProcessingChange: (Boolean) -> Unit
) {
    fun navigate(route: String) {
        if (!isProcessing) {
            onProcessingChange(true)
            navController.navigate(route)
        }
    }

    fun navigatePopUpTo(routeToNavigate: String, routePopUpTo: String) {
            navController.navigate(routeToNavigate) {
                popUpTo(routePopUpTo) { inclusive = true }
            }

    }
}