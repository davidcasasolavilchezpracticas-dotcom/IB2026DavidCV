package com.iberdrola.practicas2026.davidcv.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.iberdrola.practicas2026.davidcv.ui.screens.billlist.BillListScreen

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
    NavHost(
        navController = navController,
        startDestination = Routes.LIST,
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
            Routes.LIST
        ){
            BillListScreen(
                modifier = Modifier
            )
        }
    }
}