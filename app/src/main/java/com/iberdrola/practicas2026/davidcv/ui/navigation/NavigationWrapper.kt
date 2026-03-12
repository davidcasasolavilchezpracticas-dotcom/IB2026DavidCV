package com.iberdrola.practicas2026.davidcv.ui.navigation

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
                navController = navController
            )
        }

        composable(
            Routes.LIST_GAS
        ){
            BillListScreen(
                modifier = Modifier,
                navController = navController,
                viewSelected = false
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

            if(bsCounter == 0) {
                OpinionBottomSheet(
                    onDismiss = {
                        navController.popBackStack()
                    },
                    onLaterClick = {
                        bsCounter += 3
                        navController.popBackStack()
                    },
                    onRatingSelected = {
                        Toast.makeText(context , "Gracias por su opinión", Toast.LENGTH_SHORT).show()
                        bsCounter += 10
                        navController.popBackStack()
                    }
                )

            }
            else {
                LaunchedEffect(Unit) {
                    bsCounter -= 1
                    navController.popBackStack()
                    navController.popBackStack()
                }
            }
        }
    }
}