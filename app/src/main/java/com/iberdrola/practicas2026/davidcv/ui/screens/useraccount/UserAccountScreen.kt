package com.iberdrola.practicas2026.davidcv.ui.screens.useraccount

import androidx.activity.compose.BackHandler
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.iberdrola.practicas2026.davidcv.domain.model.account.AccountOption
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.composables.user_account.AccountOptionRow
import com.iberdrola.practicas2026.davidcv.ui.navigation.DataStoreViewModel
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserAccountScreen(
    viewModel: DataStoreViewModel = hiltViewModel(),
    navController: NavHostController,
    analytics: FirebaseAnalytics
) {
    LaunchedEffect(Unit) {
        analytics.logEvent ( "UserAccountScreen" ) {
            param("eventType", "View")
        }
    }

    BackHandler {
        navController.navigate(Routes.BACK)
        analytics.logEvent("ButtonBack") {
            param("eventType", "RelevantMovements")
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val account = uiState.account

    // Estado para controlar la animación de refresco
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val options = listOf(
        AccountOption(
            "Modificar datos",
            Icons.Outlined.Email,
            {
                navController.navigate(Routes.ACCOUNT_EDIT)
                analytics.logEvent ( "ButtonModifyAccount" ) {
                    param("eventType", "Click")
                }
            }
        )
    )

    Scaffold { padding ->
        if (uiState.isLoading && account == null) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFF006633))
            }
        } else {
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = {
                    scope.launch {
                        isRefreshing = true
                        delay(2000)
                        isRefreshing = false
                    }
                    analytics.logEvent ( "Refresh" ) {
                        param("eventType", "RelevantMovements")
                    }
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(LocalSpacing.current.xl)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (account?.profileImage != null) {
                                    AsyncImage(
                                        model = account.profileImage,
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

                            Spacer(modifier = Modifier.height(32.dp))

                            Text(
                                text = account?.name ?: "Usuario",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = account?.email ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(32.dp))
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
}
