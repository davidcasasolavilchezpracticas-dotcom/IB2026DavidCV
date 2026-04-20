package com.iberdrola.practicas2026.davidcv.ui.screens.initial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.SettingsEthernet
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.di.ConnectionMode
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
    var showConnectionMenu by remember { mutableStateOf(false) }
    var showIpDialog by remember { mutableStateOf(false) }
    var tempIp by remember { mutableStateOf(DataSourceConfig.pcIp) }

    if (showIpDialog) {
        AlertDialog(
            onDismissRequest = { showIpDialog = false },
            title = { Text(text = "Configurar IP Local") },
            text = {
                Column {
                    Text(text = "Introduce la IP de tu PC donde corre Mockoon:")
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = tempIp,
                        onValueChange = { tempIp = it },
                        label = { Text("Dirección IP") },
                        singleLine = true,
                        placeholder = { Text(text = "192.168.1.XX") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    DataSourceConfig.pcIp = tempIp
                    DataSourceConfig.connectionMode = ConnectionMode.LOCAL_IP
                    showIpDialog = false
                }) {
                    Text(text = "Confirmar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showIpDialog = false }) {
                    Text(text = "Cancelar")
                }
            }
        )
    }

    Column(
        modifier = Modifier.padding(LocalSpacing.current.xl)
    ) {
        Text(
            text = stringResource(R.string.isSubtitle),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

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

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            // Ajustes de Origen de Datos (Red vs Local)
            SettingSwitchItem(
                label = stringResource(R.string.isSwitchDataOrigin),
                checked = DataSourceConfig.useNetwork,
                onCheckedChange = { DataSourceConfig.useNetwork = it }
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (DataSourceConfig.useNetwork) {
                    Box {
                        IconButton(
                            onClick = { showConnectionMenu = true },
                            modifier = Modifier.background(
                                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                shape = CircleShape
                            )
                        ) {
                            val icon = when (DataSourceConfig.connectionMode) {
                                ConnectionMode.EMULATOR -> Icons.Default.Devices
                                ConnectionMode.ADB_REVERSE -> Icons.Default.SettingsEthernet
                                ConnectionMode.LOCAL_IP -> Icons.Default.Computer
                            }
                            Icon(imageVector = icon, contentDescription = "Modo de conexión", tint = MaterialTheme.colorScheme.primary)
                        }

                        DropdownMenu(
                            expanded = showConnectionMenu,
                            onDismissRequest = { showConnectionMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Emulador (10.0.2.2)") },
                                leadingIcon = { Icon(Icons.Default.Devices, contentDescription = null) },
                                onClick = {
                                    DataSourceConfig.connectionMode = ConnectionMode.EMULATOR
                                    showConnectionMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("ADB Reverse (127.0.0.1)") },
                                leadingIcon = { Icon(Icons.Default.SettingsEthernet, contentDescription = null) },
                                onClick = {
                                    DataSourceConfig.connectionMode = ConnectionMode.ADB_REVERSE
                                    showConnectionMenu = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("IP Local PC (${DataSourceConfig.pcIp})") },
                                leadingIcon = { Icon(Icons.Default.Computer, contentDescription = null) },
                                onClick = {
                                    tempIp = DataSourceConfig.pcIp
                                    showIpDialog = true
                                    showConnectionMenu = false
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.size(8.dp))
                }

                // Botón de Test de Error
                IconButton(
                    onClick = { throw Exception("Error de prueba") },
                    modifier = Modifier.background(
                        color = Color.Red.copy(alpha = 0.2f),
                        shape = CircleShape
                    )
                ) {
                    Icon(imageVector = Icons.Default.BugReport, contentDescription = "Error de prueba", tint = Color.Red)
                }
            }
        }
    }
}
