package com.iberdrola.practicas2026.davidcv.ui.navigation.auxiliar

import androidx.navigation.NavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.iberdrola.practicas2026.davidcv.ui.navigation.DataStoreViewModel
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes

fun handleBackNavigation(
    dataStoreViewModel: DataStoreViewModel,
    navController: NavController,
    analytics: FirebaseAnalytics,
    onNavigationStart: () -> Unit,
    onShowOpinionBS: () -> Unit,
    onNavigationEnd: () -> Unit,
    doubleBack: Boolean = false,
    currentRoute: String?,
    bsCounter: Int,
) {
    onNavigationStart()

    val goBack: () -> Unit = {
        if (doubleBack) {
            navController.popBackStack()
            navController.popBackStack()
        } else {
            navController.popBackStack()
        }
    }

    if (currentRoute == Routes.LIST_GAS || currentRoute == Routes.LIST_LIGHT) {
        if (bsCounter > 0) {
            dataStoreViewModel.updateBsCounter(bsCounter - 1)
            goBack()
        } else {
            analytics.logEvent("OpinionBottomSheet") {
                param("eventType", "View")
            }
            onShowOpinionBS()
        }
    } else {
        if (currentRoute != Routes.INITIAL && currentRoute != null) {
            goBack()
        }
    }

    onNavigationEnd()
}
