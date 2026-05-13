package com.iberdrola.practicas2026.davidcv

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.minus
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.remoteconfig.remoteConfig
import com.google.firebase.remoteconfig.remoteConfigSettings
import com.iberdrola.practicas2026.davidcv.data.workers.RefillResends
import com.iberdrola.practicas2026.davidcv.ui.navigation.NavigationWrapper
import com.iberdrola.practicas2026.davidcv.ui.theme.EnergyGreen
import com.iberdrola.practicas2026.davidcv.ui.theme.IB2026DavidCVTheme
import com.iberdrola.practicas2026.davidcv.ui.theme.White
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val refillResends = PeriodicWorkRequestBuilder<RefillResends>(
        12, TimeUnit.HOURS
    ).setConstraints(
        Constraints.Builder().build()
    ).build()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                Color.DarkGray.toArgb()
            ),

            navigationBarStyle = SystemBarStyle.dark(
                Color.DarkGray.toArgb(),
            )
        )


        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val navController = rememberNavController()
            val remoteConfig = Firebase.remoteConfig
            val analytics = Firebase.analytics
            val context = LocalContext.current

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "ReseteoDeIntentos",
                ExistingPeriodicWorkPolicy.KEEP,
                refillResends
            )

            LaunchedEffect(Unit) {
                val configSettings = remoteConfigSettings {
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
                            Log.e("RemoteConfig", "Config updated: $updated")
                        } else {
                            Log.e("RemoteConfig", "Fetch failed")
                        }
                    }
            }



            IB2026DavidCVTheme {
                var statusBarColor by remember { mutableStateOf(EnergyGreen) }
                var navigationBarColor by remember { mutableStateOf(White) }

                Scaffold(
                    topBar = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .windowInsetsTopHeight(WindowInsets.statusBars)
                                .background(statusBarColor)
                        )
                    },
                    bottomBar = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .windowInsetsBottomHeight(WindowInsets.navigationBars)
                                .background(navigationBarColor)
                        )
                    }
                ) { padding ->
                    NavigationWrapper(
                        modifier = Modifier
                            .padding(padding - padding)
                            .windowInsetsPadding(WindowInsets.safeDrawing)
                            .fillMaxSize(),
                        navController = navController,
                        remoteConfig = remoteConfig,
                        analytics = analytics,
                        colorChanger = { sbColor, nbColor ->
                            statusBarColor = sbColor
                            navigationBarColor = nbColor
                        }
                    )
                }
            }
        }
    }
}
