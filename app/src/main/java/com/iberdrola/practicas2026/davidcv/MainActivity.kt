package com.iberdrola.practicas2026.davidcv

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.navigation.NavigationWrapper
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.theme.EnergyGreen
import com.iberdrola.practicas2026.davidcv.ui.theme.IB2026DavidCVTheme
import dagger.hilt.android.AndroidEntryPoint
import com.iberdrola.practicas2026.davidcv.R


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            val remoteConfig = Firebase.remoteConfig
            val analytics = Firebase.analytics
            
            LaunchedEffect(Unit) {
                val configSettings = remoteConfigSettings {
                    // Durante desarrollo, pon esto a 0 para ver cambios inmediatos
                    minimumFetchIntervalInSeconds = 0 
                }
                remoteConfig.setConfigSettingsAsync(configSettings)

                remoteConfig.setDefaultsAsync(mapOf(
                    "ContractGasAviable" to true,
                    "ContractLightAviable" to true
                ))

                remoteConfig.fetchAndActivate()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val updated = task.result
                            Log.d("ComprobacionesRemoteConfig", "Config updated: $updated")
                        } else {
                            Log.d("ComprobacionesRemoteConfig", "Fetch failed")
                        }
                    }
            }

            IB2026DavidCVTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.safeDrawing),
                    topBar = {
                        // Solo mostramos la TopBar si NO estamos en la pantalla inicial
                        if (currentRoute != Routes.INITIAL) {
                            Row(
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(
                                    onClick = {
                                        navController.navigate(Routes.BACK)
                                    },
                                    modifier = Modifier.padding(end = LocalSpacing.current.sm),
                                    content = {
                                        Icon(
                                            imageVector = Icons.Default.ChevronLeft,
                                            contentDescription = null,
                                            tint = EnergyGreen
                                        )
                                    }
                                )
                                Text(
                                    text = stringResource(R.string.matbTitle),
                                    color = EnergyGreen
                                )
                            }
                        }
                    }
                ) {
                    NavigationWrapper(
                        modifier = Modifier,
                        navController = navController,
                        remoteConfig = remoteConfig,
                        analytics = analytics
                    )
                }
            }
        }
    }
}
