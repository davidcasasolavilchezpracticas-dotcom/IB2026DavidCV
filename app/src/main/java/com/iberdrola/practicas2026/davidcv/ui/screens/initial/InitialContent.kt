package com.iberdrola.practicas2026.davidcv.ui.screens.initial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.di.DataSourceConfig
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.composables.initial.ContractSection
import com.iberdrola.practicas2026.davidcv.ui.base.composables.initial.ServiceSection
import com.iberdrola.practicas2026.davidcv.ui.base.composables.initial.SettingSwitchItem
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes

@Composable
fun InitialContent(
    navController: NavHostController,
    analytics: FirebaseAnalytics,
    isGasActive: Boolean,
    isLightActive: Boolean
) {
    Column(modifier = Modifier.padding(LocalSpacing.current.xl)) {
        Text(stringResource(R.string.isSubtitle), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.weight(2f))

        // Sección Facturas
        ServiceSection(
            title = stringResource(R.string.isSubtitleBills),
            isLightActive = isLightActive,
            isGasActive = isGasActive,
            onLightClick = { navController.navigate(Routes.LIST_LIGHT) },
            onGasClick = { navController.navigate(Routes.LIST_GAS) },
            analytics = analytics
        )

        Spacer(modifier = Modifier.weight(2f))

        // Sección Contratos
        ContractSection {
            navController.navigate(Routes.CONTRACTS)
            analytics.logEvent("ButtonContractsList")
                { param("eventType", "Click") }
        }

        Spacer(modifier = Modifier.weight(2f))

        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ){
            // Ajustes
            SettingSwitchItem(
                label = stringResource(R.string.isSwitchDataOrigin),
                checked = DataSourceConfig.useNetwork,
                onCheckedChange = { DataSourceConfig.useNetwork = it }
            )

            IconButton(
                onClick = {
                    throw Exception("Error de prueba")
                },
                modifier = Modifier
                    .background(
                        color = Color.Red.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.BugReport,
                    contentDescription = "Error de prueba"
                )
            }
        }
    }
}