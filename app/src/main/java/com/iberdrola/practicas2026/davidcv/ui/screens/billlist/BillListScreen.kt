package com.iberdrola.practicas2026.davidcv.ui.screens.billlist

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.di.DataSourceConfig
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content.BillListHeader
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content.HorizontalPage
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content.TabItem
import com.iberdrola.practicas2026.davidcv.ui.base.screens.LoadingScreen
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.screens.billfilter.BillFilterState
import com.iberdrola.practicas2026.davidcv.ui.theme.DividerGray
import kotlinx.coroutines.launch

/**
 * BillListScreen
 * Se define la pantalla que muestra el listado de facturas con soporte para deslizamiento entre tipos
 *
 * @param viewModel
 * @param navController
 * @param modifier
 * @param viewSelected
 */
@Composable
fun BillListScreen(
    viewModel: BillListViewModel = hiltViewModel(),
    navController: NavController,
    modifier: Modifier,
    viewSelected: Boolean = true,
    analytics: FirebaseAnalytics,
    remoteConfig: FirebaseRemoteConfig
) {
    LaunchedEffect(Unit) {
        analytics.logEvent("BillListScreen") {
            param("eventType", "View")
        }
    }

    // Simplificación de Remote Config: Leemos los valores una vez o usamos un estado
    val isGasActive = remember { remoteConfig.getBoolean("ContractGasAviable") }
    val isLightActive = remember { remoteConfig.getBoolean("ContractLightAviable") }

    val lightBillsState by viewModel.lightBillsState.collectAsStateWithLifecycle()
    val gasBillsState by viewModel.gasBillsState.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(
        initialPage =
            if (isLightActive && isGasActive && viewSelected) 0
            else if (isLightActive && isGasActive) 1
            else 0
        ,
        pageCount = {
            if (isLightActive && isGasActive) 2
            else 1
        }
    )

    // Observar el resultado de los filtros desde el SavedStateHandle de la navegación
    val filterResult by navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getLiveData<BillFilterState>("filters_result")
        ?.observeAsState()
        ?: remember { mutableStateOf(null) }

    LaunchedEffect(filterResult) {
        filterResult?.let { filters ->
            viewModel.applyFilters(filters)
            analytics.logEvent("ApplyFilters") {
                param("eventType", "RelevantMovements")
            }
        }
    }

    BackHandler {
        navController.navigate(Routes.BACK)
        analytics.logEvent("ButtonBack") {
            param("eventType", "RelevantMovements")
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getLightBills()
        viewModel.getGasBills()
    }


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        BillListHeader(
            modifier = modifier.padding(start = LocalSpacing.current.lg),
        )

        Spacer(modifier = modifier.height(4.dp))

        HorizontalPage(
            lightBillsState = lightBillsState,
            gasBillsState = gasBillsState,
            navController = navController,
            pagerState = pagerState,
            modifier = modifier,
            isLightActive = isLightActive,
            isGasActive = isGasActive,
            analytics = analytics
        )
    }
}

