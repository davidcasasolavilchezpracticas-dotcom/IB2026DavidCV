package com.iberdrola.practicas2026.davidcv.ui.screens.initial


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InitialScreen(
    navController: NavHostController,
    modifier: Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hola, Juan", style = MaterialTheme.typography.headlineSmall) },
                actions = {
                    IconButton(onClick = { /* Perfil */ }) {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Tarjeta de Resumen Rápido
            SummaryCard({ navController.navigate("list") })

            // Otras secciones (Ejemplo: Consumo actual)
            Text(text = "Mis servicios", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                ServiceItem(Icons.Default.Lightbulb, "Luz", Modifier.weight(1f))
                ServiceItem(Icons.Default.LocalGasStation, "Gas", Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun SummaryCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF006633))
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text("Tu última factura", color = Color.White.copy(alpha = 0.8f))
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("20,00 €", style = MaterialTheme.typography.headlineLarge, color = Color.White)
                Button(
                    onClick = onClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Ver facturas", color = Color(0xFF006633))
                }
            }
        }
    }
}

@Composable
fun ServiceItem(icon: ImageVector, label: String, modifier: Modifier = Modifier) {
    OutlinedCard(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = Color(0xFF006633))
            Spacer(modifier = Modifier.height(8.dp))
            Text(label, fontWeight = FontWeight.Medium)
        }
    }
}
