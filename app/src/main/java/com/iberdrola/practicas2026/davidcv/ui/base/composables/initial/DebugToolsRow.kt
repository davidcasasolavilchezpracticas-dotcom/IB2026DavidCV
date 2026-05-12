package com.iberdrola.practicas2026.davidcv.ui.base.composables.initial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.di.ConnectionMode

@Composable
fun DebugToolsRow(
    useNetwork: Boolean,
    connectionMode: ConnectionMode,
    pcIp: String,
    onNetworkToggle: (Boolean) -> Unit,
    onModeChange: (ConnectionMode) -> Unit,
    onIpConfigClick: () -> Unit,
    onTestErrorClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (useNetwork) {
                ConnectionModeSelector(
                    currentMode = connectionMode,
                    pcIp = pcIp,
                    onModeSelected = onModeChange,
                    onConfigIpClick = onIpConfigClick
                )
                Spacer(modifier = Modifier.size(8.dp))
            }

            SettingSwitchItem(
                onCheckedChange = onNetworkToggle,
                label = stringResource(R.string.isSwitchDataOrigin),
                checked = useNetwork,
            )

            Spacer(modifier = Modifier.size(8.dp))

            IconButton(
                onClick = onTestErrorClick,
                modifier = Modifier.background(Color.Red.copy(alpha = 0.2f), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.BugReport,
                    contentDescription = "Test Error",
                    tint = Color.Red
                )
            }
        }
    }
}