package com.iberdrola.practicas2026.davidcv.ui.base.composables.initial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.SettingsEthernet
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.di.ConnectionMode
import com.iberdrola.practicas2026.davidcv.ui.theme.White

@Composable
fun ConnectionModeSelector(
    currentMode: ConnectionMode,
    pcIp: String,
    onModeSelected: (ConnectionMode) -> Unit,
    onConfigIpClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(
            onClick = { expanded = true },
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = CircleShape
            )
        ) {
            val icon = when (currentMode) {
                ConnectionMode.ADB_REVERSE -> Icons.Default.SettingsEthernet
                ConnectionMode.EMULATOR -> Icons.Default.Devices
                ConnectionMode.LOCAL_IP -> Icons.Default.Computer
            }
            Icon(
                imageVector = icon,
                contentDescription = stringResource(R.string.descriptionConexionButton),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(White)
        ) {
            ConnectionMenuItem(Icons.Default.Devices, R.string.dropMenuItemTextEmulator) {
                onModeSelected(ConnectionMode.EMULATOR); expanded = false
            }
            ConnectionMenuItem(Icons.Default.SettingsEthernet, R.string.dropMenuItemTextADB) {
                onModeSelected(ConnectionMode.ADB_REVERSE); expanded = false
            }
            ConnectionMenuItem(Icons.Default.Computer, R.string.dropMenuItemTextIP, suffix = " ($pcIp)") {
                onConfigIpClick(); expanded = false
            }
        }
    }
}

