package com.iberdrola.practicas2026.davidcv.ui.navigation.auxiliar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import java.util.Locale

@Composable
fun NavigationAnalyticsObserver(
    navController: NavController,
    analytics: FirebaseAnalytics
) {
    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { controller, destination, _ ->
            // 1. Extraer nombres de rutas
            val route = destination.route ?: "unknown"
            val originRoute = controller.previousBackStackEntry?.destination?.route ?: "Start"

            // 2. Formatear el nombre del evento (ej: FromInitialToContracts)
            val eventName = "From${originRoute.capitalize()}To${route.capitalize()}"

            // 3. Loggear en Firebase
            analytics.logEvent(eventName) {
                param("origin_screen", originRoute)
                param("destination_screen", route)
                param("event_type", "Movement")
            }
        }

        // Suscribirse
        navController.addOnDestinationChangedListener(listener)

        // Limpiar al destruir el componente
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
}

/**
 * Función de extensión auxiliar para capitalizar nombres de rutas
 */
private fun String.capitalize(): String = this.replaceFirstChar {
    if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
}