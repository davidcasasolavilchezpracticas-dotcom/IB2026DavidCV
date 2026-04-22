package com.iberdrola.practicas2026.davidcv.ui.navigation

import androidx.navigation.NavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent

fun handleBackNavigation(
    currentRoute: String?,
    navController: NavController,
    bsCounter: Int,
    dataStoreViewModel: DataStoreViewModel,
    analytics: FirebaseAnalytics,
    onShowOpinionBS: () -> Unit
) {
    if (currentRoute == Routes.LIST_GAS || currentRoute == Routes.LIST_LIGHT) {
        if (bsCounter > 0) {
            dataStoreViewModel.updateBsCounter(bsCounter - 1)
            navController.popBackStack()
        } else {
            analytics.logEvent("OpinionBottomSheet") {
                param("eventType", "View")
            }
            onShowOpinionBS()
        }
    } else {
        if (currentRoute != Routes.INITIAL && currentRoute != null) {
            navController.popBackStack()
        }
    }
}