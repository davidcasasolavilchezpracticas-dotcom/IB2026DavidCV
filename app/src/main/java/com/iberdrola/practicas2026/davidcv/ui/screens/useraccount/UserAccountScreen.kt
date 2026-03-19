package com.iberdrola.practicas2026.davidcv.ui.screens.useraccount

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.iberdrola.practicas2026.davidcv.domain.model.account.AccountOption
import com.iberdrola.practicas2026.davidcv.ui.base.composables.user_account.AccountOptionRow
import com.iberdrola.practicas2026.davidcv.ui.navigation.DataStoreViewModel
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAccountScreen(
    viewModel: DataStoreViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val account by viewModel.account.collectAsStateWithLifecycle()

    // Estado para controlar la animación de refresco
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val options = listOf(
        AccountOption(
            "Modificar datos",
            Icons.Outlined.Email,
            {
                navController.navigate(Routes.ACCOUNT_EDIT)
            }
        )
    )

    Scaffold { padding ->
        // PullToRefreshBox envuelve el contenido desplazable
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = {
                scope.launch {
                    isRefreshing = true
                    // Simulamos una carga de datos de red (2 segundos)
                    delay(2000)
                    // Aquí podrías llamar a una función para refrescar la foto:
                    // viewModel.updateAccountInfo(...) 
                    isRefreshing = false
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                // HEADER: Información del usuario
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Círculo de imagen de perfil
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            // Verificamos si hay imagen. Si es un ImageVector o URL, AsyncImage lo maneja.
                            if (account?.profileImage != null) {
                                AsyncImage(
                                    model = account?.profileImage,
                                    contentDescription = "Imagen de perfil",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(60.dp),
                                    tint = Color(0xFF006633)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = account?.name ?: "Usuario",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = account?.email ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }

                item {
                    Text(
                        text = "Gestión de cuenta",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                // Opciones del menú
                items(options) { option ->
                    AccountOptionRow(option)
                }

                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "ID de usuario: ${account?.id ?: 0} | Versión 2.4.1",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
