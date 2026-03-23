package com.iberdrola.practicas2026.davidcv.ui.screens.initial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.di.DataSourceConfig
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.composables.initial.ServiceItem
import com.iberdrola.practicas2026.davidcv.ui.base.composables.initial.SettingSwitchItem
import com.iberdrola.practicas2026.davidcv.ui.base.composables.initial.SummaryCard
import com.iberdrola.practicas2026.davidcv.ui.navigation.DataStoreViewModel
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.theme.White

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
    modifier: Modifier = Modifier,
    dataStoreViewModel: DataStoreViewModel = hiltViewModel(),
    analytics: FirebaseAnalytics
) {
    val account by dataStoreViewModel.account.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        analytics.logEvent ( "InitialScreen" ) {
            param("eventType", "View")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text =  "",//stringResource(R.string.isTitlePage),
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(Routes.ACCOUNT_INFO)
                            analytics.logEvent ( "ButtonAccountInfo" ) {
                                param("eventType", "Click")
                            }
                        }
                    ) {
                        if (account?.profileImage == null) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                modifier = Modifier.size(60.dp),
                                tint = Color(0xFF006633)
                            )
                        } else {
                            AsyncImage(
                                model = account?.profileImage,
                                contentDescription = "Imagen de perfil",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF006633),
                    titleContentColor = White
                )
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF006633))
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {


            Text(
                text =  if (account == null)
                    stringResource(R.string.isTitle)
                else
                    stringResource(R.string.isTitleLogged) + " ${account?.name}",
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 28.sp),
                modifier = Modifier.
                    padding(horizontal = LocalSpacing.current.xl),
                fontWeight = FontWeight.Bold,
                color = White
            )

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = White,
                        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
                    )
            ){
                Column (
                    modifier = Modifier.
                        padding(LocalSpacing.current.xl),
                ){
                    Text(
                        text = stringResource(R.string.isSubtitle),
                        style = MaterialTheme.typography.titleLarge.copy(fontSize = 24.sp),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.weight(2f))

                    Text(
                        text = stringResource(R.string.isSubtitleBills),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ServiceItem(
                            icon = Icons.Default.Lightbulb,
                            label = stringResource(R.string.isServiceLight),
                            modifier = Modifier.weight(1f),
                            onClick = {
                                navController.navigate(Routes.LIST_LIGHT)
                                analytics.logEvent("ButtonLightBills") {
                                    param("eventType", "Click")
                                }
                            },
                        )
                        ServiceItem(
                            icon = Icons.Default.LocalGasStation,
                            label = stringResource(R.string.isServiceGas),
                            modifier = Modifier.weight(1f),
                            onClick = {
                                navController.navigate(Routes.LIST_GAS)
                                analytics.logEvent("ButtonGasBills") {
                                    param("eventType", "Click")
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.weight(2f))


                    Text(
                        text = stringResource(R.string.isSubtitleContracts),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.weight(1f))


                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ServiceItem(
                            icon = Icons.Default.Description,
                            label = stringResource(R.string.isServiceContract),
                            modifier = Modifier.weight(1f),
                            onClick = {
                                navController.navigate(Routes.CONTRACTS)
                                analytics.logEvent("ButtonContractsList") {
                                    param("eventType", "Click")
                                }
                            }
                        )
                    }

                    Spacer(modifier = Modifier.weight(2f))

                    SettingSwitchItem(
                        label = stringResource(R.string.isSwitchDataOrigin),
                        checked = DataSourceConfig.useNetwork,
                        onCheckedChange = {
                            DataSourceConfig.useNetwork = it
                            analytics.logEvent("SwitchDataOrigin") {
                                param("eventType", "RelevantMovements")
                            }
                        }
                    )
                }
            }
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
        modifier = Modifier,
        analytics = FirebaseAnalytics.getInstance(navController.context)
    )
}
