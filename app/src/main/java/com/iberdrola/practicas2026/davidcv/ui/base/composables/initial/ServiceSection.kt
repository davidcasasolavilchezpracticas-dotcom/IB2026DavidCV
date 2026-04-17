package com.iberdrola.practicas2026.davidcv.ui.base.composables.initial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.iberdrola.practicas2026.davidcv.R

@Composable
fun ServiceSection(
    title: String,
    isLightActive: Boolean,
    isGasActive: Boolean,
    onLightClick: () -> Unit,
    onGasClick: () -> Unit,
    analytics: FirebaseAnalytics
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (isLightActive) {
                ServiceItem(
                    icon = Icons.Default.Lightbulb,
                    label = stringResource(R.string.isServiceLight),
                    modifier = Modifier.weight(1f)
                ) {
                    onLightClick()
                    analytics.logEvent("ButtonLightBills")
                        { param("eventType", "Click") }
                }
            }
            if (isGasActive) {
                ServiceItem(
                    icon = Icons.Default.LocalGasStation,
                    label = stringResource(R.string.isServiceGas),
                    modifier = Modifier.weight(1f)
                ) {
                    onGasClick()
                    analytics.logEvent("ButtonGasBills")
                        { param("eventType", "Click") }
                }
            }

            if (!isLightActive && !isGasActive) {
                Text(
                    text = stringResource(R.string.isServiceOr),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}