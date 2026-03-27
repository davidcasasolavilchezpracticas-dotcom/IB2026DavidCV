package com.iberdrola.practicas2026.davidcv.ui.screens.initial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.iberdrola.practicas2026.davidcv.ui.base.composables.initial.InitialTopBar
import com.iberdrola.practicas2026.davidcv.ui.base.composables.initial.WelcomeHeader
import com.iberdrola.practicas2026.davidcv.ui.navigation.DataStoreViewModel
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.theme.White

/**
 * InitialScreen
 * Se define la pantalla principal de la aplicación con el resumen de servicios
 *
 * @param navController
 * @param modifier
 */@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InitialScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    dataStoreViewModel: DataStoreViewModel = hiltViewModel(),
    analytics: FirebaseAnalytics,
    remoteConfig: FirebaseRemoteConfig
) {
    val account by dataStoreViewModel.account.collectAsStateWithLifecycle()

    // Simplificación de Remote Config: Leemos los valores una vez o usamos un estado
    val isGasActive = remember { remoteConfig.getBoolean("ContractGasAviable") }
    val isLightActive = remember { remoteConfig.getBoolean("ContractLightAviable") }

    LaunchedEffect(Unit) {
        analytics.logEvent("InitialScreen")
            { param("eventType", "View") }
    }

    Scaffold(
        topBar = {
            InitialTopBar(account) {
                navController.navigate(Routes.ACCOUNT_INFO)
                analytics.logEvent("ButtonAccountInfo")
                    { param("eventType", "Click") }
            }
        },
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFF006633))
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            WelcomeHeader(account?.name)

            // Contenedor principal blanco
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = White,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                InitialContent(
                    navController = navController,
                    analytics = analytics,
                    isGasActive = isGasActive,
                    isLightActive = isLightActive
                )
            }
        }
    }
}

/**
 * InitialScreenPreview
 * Vista previa de la pantalla inicial
 */
@Preview
@Composable
fun InitialScreenPreview() {
    val navController = rememberNavController()
    InitialScreen(
        navController = navController,
        modifier = Modifier,
        analytics = FirebaseAnalytics.getInstance(navController.context),
        remoteConfig = FirebaseRemoteConfig.getInstance()
    )
}
