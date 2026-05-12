package com.iberdrola.practicas2026.davidcv.ui.screens.initial

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.iberdrola.practicas2026.davidcv.data.remote.firebase.RemoteConfigConstants
import com.iberdrola.practicas2026.davidcv.ui.base.composables.initial.WelcomeHeader
import com.iberdrola.practicas2026.davidcv.ui.theme.EnergyGreen
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
    remoteConfig: FirebaseRemoteConfig,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onNavigate: (String) -> Unit,
    analytics: FirebaseAnalytics,
) {
    val view = LocalView.current
    val window = (view.context as Activity).window

    val isFirstRun = remoteConfig.info.lastFetchStatus == FirebaseRemoteConfig.LAST_FETCH_STATUS_NO_FETCH_YET

    var isGasActive by remember {
        mutableStateOf(if (isFirstRun) true else remoteConfig.getBoolean(RemoteConfigConstants.ACTIVATE_GAS))
    }
    var isLightActive by remember {
        mutableStateOf(if (isFirstRun) true else remoteConfig.getBoolean(RemoteConfigConstants.ACTIVATE_LIGHT))
    }

    DisposableEffect(Unit) {
        onDispose {
            val windowInsetsController = WindowCompat.getInsetsController(window, view)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

            windowInsetsController.isAppearanceLightStatusBars = true
        }
    }


    LaunchedEffect(Unit) {
        isGasActive = remoteConfig.getBoolean(RemoteConfigConstants.ACTIVATE_GAS)
        isLightActive = remoteConfig.getBoolean(RemoteConfigConstants.ACTIVATE_LIGHT)

        remoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                isGasActive = remoteConfig.getBoolean(RemoteConfigConstants.ACTIVATE_GAS)
                isLightActive = remoteConfig.getBoolean(RemoteConfigConstants.ACTIVATE_LIGHT)
            }
        }
        analytics.logEvent("InitialScreen")
            { param("eventType", "View") }
    }

    Scaffold { padding ->
        Column(
            modifier = modifier
                .statusBarsPadding()
                .background(EnergyGreen)
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            WelcomeHeader()

            Surface(
                modifier = Modifier.fillMaxSize(),
                color = White,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                InitialContent(
                    isLightActive = isLightActive,
                    onNavigate = onNavigate,
                    isGasActive = isGasActive,
                    analytics = analytics,
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
        remoteConfig = FirebaseRemoteConfig.getInstance(),
        onNavigate = {},
    )
}
