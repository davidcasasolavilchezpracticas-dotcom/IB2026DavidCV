package com.iberdrola.practicas2026.davidcv.ui.navigation

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.iberdrola.practicas2026.davidcv.ui.base.screens.OpinionBottomSheet
import com.iberdrola.practicas2026.davidcv.ui.screens.billlist.BillListScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.initial.InitialScreen

/**
 * Nav host screen
 * Se define el contenedor del grafo de navegación
 *
 * @param navController
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationWrapper(
    modifier: Modifier,
    navController: NavHostController,
){
    var bsCounter by rememberSaveable { mutableIntStateOf(0) }
    var viewSelected by rememberSaveable { mutableStateOf(true) }

    /*// Observar y loguear toda la cola de navegación
    LaunchedEffect(navController) {
        navController.currentBackStack.collect { stack ->
            val debugStack = stack
                .mapNotNull { entry -> entry.destination.route } // Filtramos solo los que tienen ruta
                .joinToString(separator = "  |  ")

            Log.d("NavigationStack", "COLA ACTUAL: [ $debugStack ]")
            Log.d("NavigationStack", "Tamaño de la cola: ${stack.size}")
        }
    }*/

    NavHost(
        navController = navController,
        startDestination = Routes.INITIAL,
        modifier = modifier,
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(1500)
            ) + fadeIn()
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(1500)
            ) + fadeOut()
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(1500)
            ) + fadeIn()
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(1500)
            ) + fadeOut()
        }
    ){
        //º Se define la navegación de nuestra aplicación vinculada

        composable(
            Routes.LIST_LIGHT
        ){
            BillListScreen(
                modifier = Modifier,
                navController = navController,
                viewSelected = viewSelected
            )
        }

        composable(
            Routes.LIST_GAS
        ){
            BillListScreen(
                modifier = Modifier,
                navController = navController,
                viewSelected = !viewSelected
            )
        }

        composable(
            Routes.INITIAL
        ){
            InitialScreen(
                navController = navController,
                modifier = Modifier
            )
        }

        composable(
            Routes.BACK
        ) {
            val context = LocalContext.current

            //navController.
                //.value.forEach{ Log.d("Comprobaciones", "popstack: ${it}") }

            if (bsCounter > 0) {
                LaunchedEffect(Unit) {
                    bsCounter -= 1
                    navController.popBackStack()
                    navController.popBackStack()
                }
            } else {
                // Si el contador es 0, mostramos el BottomSheet
                OpinionBottomSheet(
                    onDismiss = {
                        navController.popBackStack()
                    },
                    onLaterClick = {
                        // Si pulsa luego, sumamos 3 para que no vuelva a salir pronto
                        bsCounter = 3
                        //navController.popBackStack()
                    },
                    onRatingSelected = {
                        Toast.makeText(context, "Gracias por su opinión", Toast.LENGTH_SHORT).show()
                        // Si puntúa, sumamos mucho para que no salga casi nunca
                        bsCounter = 10
                        //navController.popBackStack()
                    }
                )
            }
        }
    }
}