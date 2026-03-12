package com.iberdrola.practicas2026.davidcv.ui.screens.initial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.iberdrola.practicas2026.davidcv.ui.base.composables.ServiceItem
import com.iberdrola.practicas2026.davidcv.ui.base.composables.SummaryCard
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InitialScreen(
    navController: NavHostController,
    modifier: Modifier,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Hola, Juan",
                        style = MaterialTheme.typography.headlineSmall
                        )
                    },
                actions = {
                    IconButton(onClick = { /* Perfil */ }) {
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
                    .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            // Tarjeta de Resumen Rápido
            SummaryCard(
                onClick = { navController.navigate(Routes.LIST_LIGHT) }
            )

            // Otras secciones (Ejemplo: Consumo actual)
            Text(
                text = "Mis servicios",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ServiceItem(
                    icon = Icons.Default.Lightbulb,
                    label = "Luz",
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(Routes.LIST_LIGHT) },
                )
                ServiceItem(
                    icon = Icons.Default.LocalGasStation,
                    label = "Gas",
                    modifier = Modifier.weight(1f),
                    onClick = { navController.navigate(Routes.LIST_GAS) }
                )
            }
        }
    }
}






