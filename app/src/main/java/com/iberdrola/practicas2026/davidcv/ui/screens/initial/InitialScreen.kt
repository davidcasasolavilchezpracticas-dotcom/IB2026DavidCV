package com.iberdrola.practicas2026.davidcv.ui.screens.initial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.di.DataSourceConfig
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.composables.initial.ServiceItem
import com.iberdrola.practicas2026.davidcv.ui.base.composables.initial.SettingSwitchItem
import com.iberdrola.practicas2026.davidcv.ui.base.composables.initial.SummaryCard
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes

/**
 * InitialScreen
 * Se define la pantalla principal de la aplicación con el resumen de servicios
 *
 * @param navController
 * @param modifier
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InitialScreen(
    navController: NavHostController,
    modifier: Modifier
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.isTitle),
                        style = MaterialTheme.typography.headlineSmall
                        )
                    },
                actions = {
                    IconButton(onClick = { /*TODO Perfil */ }) {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                        )
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(LocalSpacing.current.lg),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            SummaryCard(
                onClick = { navController.navigate(Routes.LIST_LIGHT) }
            )

            Text(
                text = stringResource(R.string.isSubtitle),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = stringResource(R.string.isSubtitleBills),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ServiceItem(
                    icon = Icons.Default.Lightbulb,
                    label = stringResource(R.string.isServiceLight),
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(Routes.LIST_LIGHT) },
                )
                ServiceItem(
                    icon = Icons.Default.LocalGasStation,
                    label = stringResource(R.string.isServiceGas),
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(Routes.LIST_GAS) }
                )
            }

            Text(
                text = stringResource(R.string.isSubtitleContracts),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ServiceItem(
                    icon = Icons.Default.Description,
                    label = stringResource(R.string.isServiceContract),
                    modifier = Modifier.weight(1f),
                    onClick = { /*TODO Navigate a List Contracts*/ }
                )
            }

            SettingSwitchItem(
                label = stringResource(R.string.isSwitchDataOrigin),
                checked = DataSourceConfig.useNetwork,
                onCheckedChange = {
                    DataSourceConfig.useNetwork = it
                }
            )
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
        modifier = Modifier
    )
}
