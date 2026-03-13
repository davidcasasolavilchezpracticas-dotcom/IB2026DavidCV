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
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.iberdrola.practicas2026.davidcv.ui.base.screens.OpinionBottomSheet
import com.iberdrola.practicas2026.davidcv.ui.screens.billlist.BillListScreen
import com.iberdrola.practicas2026.davidcv.ui.screens.initial.InitialScreen

/**
 * NavigationWrapper
 * Se define el contenedor del grafo de navegación
 *
 * @param navController
 * @param modifier
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavigationWrapper(
    modifier: Modifier,
    navController: NavHostController,
){
    var bsCounter by rememberSaveable { mutableIntStateOf(0) }
    var viewSelected by rememberSaveable { mutableStateOf(true) }


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

            if(navController.currentDestination != NavDestination(Routes.INITIAL)){
                if (bsCounter > 0) {
                    LaunchedEffect(Unit) {
                        bsCounter -= 1
                        navController.popBackStack()
                        navController.popBackStack()
                    }
                } else {
                    OpinionBottomSheet(
                        onDismiss = {
                            navController.popBackStack()
                        },
                        onLaterClick = {
                            bsCounter = 3
                        },
                        onRatingSelected = {
                            Toast.makeText(context, "Gracias por su opinión", Toast.LENGTH_SHORT)
                                .show()
                            bsCounter = 10
                        }
                    )
                }
            }
        }
    }
}