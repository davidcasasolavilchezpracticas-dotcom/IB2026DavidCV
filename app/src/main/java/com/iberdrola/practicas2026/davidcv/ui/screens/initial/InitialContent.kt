package com.iberdrola.practicas2026.davidcv.ui.screens.initial

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.di.ConnectionMode
import com.iberdrola.practicas2026.davidcv.domain.di.DataSourceConfig
import com.iberdrola.practicas2026.davidcv.ui.base.common.ClickEventManager
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalClickManager
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.common.SafeClickTools.Companion.canExecuteMethod
import com.iberdrola.practicas2026.davidcv.ui.base.composables.initial.ContractSection
import com.iberdrola.practicas2026.davidcv.ui.base.composables.initial.DebugToolsRow
import com.iberdrola.practicas2026.davidcv.ui.base.composables.initial.IpConfigurationDialog
import com.iberdrola.practicas2026.davidcv.ui.base.composables.initial.ServiceSection
import com.iberdrola.practicas2026.davidcv.ui.base.composables.initial.SettingSwitchItem
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.theme.White

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun InitialContent(
    onNavigate: (String) -> Unit,
    analytics: FirebaseAnalytics,
    isGasActive: Boolean,
    isLightActive: Boolean
) {
    var showIpDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val clickManager = remember { ClickEventManager() }

    CompositionLocalProvider(LocalClickManager provides clickManager) {
        val manager = LocalClickManager.current

        if (showIpDialog) {
            IpConfigurationDialog(
                initialIp = DataSourceConfig.pcIp,
                onDismiss = { showIpDialog = false },
                onConfirm = { newIp ->
                    DataSourceConfig.pcIp = newIp
                    DataSourceConfig.connectionMode = ConnectionMode.LOCAL_IP
                    showIpDialog = false
                }
            )
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(LocalSpacing.current.xl)
        ) {
            Text(
                text = stringResource(R.string.isSubtitle),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.weight(2f))

            ServiceSection(
                onLightClick = {
                    canExecuteMethod(
                        manager,
                    ) { onNavigate(Routes.LIST_LIGHT) }
                },
                onGasClick = {
                    canExecuteMethod(
                        manager
                    ) { onNavigate(Routes.LIST_GAS) }
                },
                title = stringResource(R.string.isSubtitleBills),
                isLightActive = isLightActive,
                isGasActive = isGasActive,
                analytics = analytics
            )

            Spacer(modifier = Modifier.weight(2f))

            ContractSection {
                canExecuteMethod(
                    manager
                ) {
                    onNavigate(Routes.CONTRACTS)
                    analytics.logEvent("ButtonContractsList") { param("eventType", "Click") }
                }
            }

            Spacer(modifier = Modifier.weight(2f))

            DebugToolsRow(
                useNetwork = DataSourceConfig.useNetwork,
                connectionMode = DataSourceConfig.connectionMode,
                pcIp = DataSourceConfig.pcIp,
                onNetworkToggle = {
                    canExecuteMethod(
                        manager
                    ) { DataSourceConfig.useNetwork = it }
                },
                onModeChange = {
                    canExecuteMethod(
                        manager
                    ) { DataSourceConfig.connectionMode = it }
                },
                onIpConfigClick = {
                    canExecuteMethod(
                        manager
                    ) { showIpDialog = true }
                },
                onTestErrorClick = {
                    canExecuteMethod(
                        manager
                    ) { throw Exception(context.getString(R.string.testError)) }
                },
            )
        }
    }
}

