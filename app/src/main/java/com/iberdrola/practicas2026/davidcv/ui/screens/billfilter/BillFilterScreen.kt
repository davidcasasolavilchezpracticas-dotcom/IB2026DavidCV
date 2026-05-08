package com.iberdrola.practicas2026.davidcv.ui.screens.billfilter

import DateSelector
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.model.bill.PaymentStatus
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billfilter.FilterOption
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billfilter.PriceRangeSelector
import com.iberdrola.practicas2026.davidcv.ui.theme.White
import java.time.LocalDateTime

/**
 * FilterScreen
 * Componente que muestra la pantalla de filtros de búsqueda de facturas.
 *
 * @param navController Controlador de navegación de Jetpack Compose
 * @param viewModel Modelo de vista asociado a la pantalla de filtros
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterScreen(
    viewModel: BillViewModel = hiltViewModel(),
    navController: NavController,
    analytics: FirebaseAnalytics,
    isProcesing: Boolean,
    onBack: () -> Unit,
) {
    val context = LocalContext.current

    // Recuperamos los datos de navegación inmediatamente
    val backStackEntry = remember { navController.previousBackStackEntry }
    val initialFilters = remember { backStackEntry?.savedStateHandle?.get<BillFilterState>("initial_filters") }
    val minDateLimitStr = remember { backStackEntry?.savedStateHandle?.get<String>("min_date_limit") }
    val maxDateLimitStr = remember { backStackEntry?.savedStateHandle?.get<String>("max_date_limit") }
    val minLimit = remember { backStackEntry?.savedStateHandle?.get<Float>("min_limit") }
    val maxLimit = remember { backStackEntry?.savedStateHandle?.get<Float>("max_limit") }

    val minDateLimit = remember(minDateLimitStr) { try { minDateLimitStr?.let { LocalDateTime.parse(it) } } catch (e: Exception) { null } }
    val maxDateLimit = remember(maxDateLimitStr) { try { maxDateLimitStr?.let { LocalDateTime.parse(it) } } catch (e: Exception) { null } }

    // Sincronizamos con el ViewModel al cargar
    LaunchedEffect(initialFilters, minLimit, maxLimit, minDateLimit, maxDateLimit) {
        viewModel.setInitialFilters(
            filters = initialFilters ?: BillFilterState(),
            minDate = minDateLimit,
            maxDate = maxDateLimit,
            min = minLimit,
            max = maxLimit,
        )
        analytics.logEvent ( "FilterScreen" ) {
            param("eventType", "View")
        }
    }


    var applyButtonEnabled by remember { mutableStateOf(true) }
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    BackHandler {
        onBack()
        analytics.logEvent("ButtonBack") {
            param("eventType", "RelevantMovements")
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = LocalSpacing.current.la),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column {
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(R.string.fsTituloFiltros),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(R.string.fsTituloFecha),
                    fontWeight = FontWeight.Bold
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    DateSelector(
                        label = stringResource(R.string.fsSubtituloFecha1),
                        date = state.startDate,
                        modifier = Modifier.weight(1f),
                        onConfirm = {
                            date -> viewModel.onStartDateSelected(date, context)
                            analytics.logEvent ( "SetStartDate" ) {
                                param("eventType", "RelevantMovements")
                            }
                        },
                        onValidDate = viewModel::onValidStartDate,
                        maxDate = state.endDate?.minusDays(1) ?: maxDateLimit,
                        minDate = minDateLimit,
                    )
                    DateSelector(
                        label = stringResource(R.string.fsSubtituloFecha2),
                        date = state.endDate,
                        modifier = Modifier.weight(1f),
                        onConfirm = {
                            date -> viewModel.onEndDateSelected(date, context)
                            analytics.logEvent ( "SetEndDate" ) {
                                param("eventType", "RelevantMovements")
                            }
                        },
                        onValidDate = viewModel::onValidEndDate,
                        minDate = state.startDate?.plusDays(1) ?: minDateLimit,
                        maxDate = maxDateLimit,
                    )
                }
            }

            // Priorizamos los límites reales del ViewModel, pero usamos los pasados por navegación como respaldo inmediato
            val currentMin = minLimit ?: 0f
            val currentMax = maxLimit ?: 500f

            PriceRangeSelector(
                selectedRange = state.priceRange ?: (currentMin..currentMax),
                totalRange = currentMin..currentMax,
                onSliderChange = {
                    range -> viewModel.onPriceRangeChanged(range)
                    analytics.logEvent ( "SetPriceRange" ) {
                        param("eventType", "RelevantMovements")
                    }
                },
            )

            Column {
                Text(
                    text = stringResource(R.string.fsTituloEstado),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                )

                Spacer(modifier = Modifier.height(4.dp))

                FilterOption(
                    onCheckedChange = viewModel::onStateChangePaid,
                    label = PaymentStatus.PAID.label,
                    value = state.paymentStatusPaid,
                )

                Spacer(modifier = Modifier.height(8.dp))

                FilterOption(
                    onCheckedChange = viewModel::onStateChangePending,
                    label = PaymentStatus.PENDING.label,
                    value = state.paymentStatusPending,
                )

                Spacer(modifier = Modifier.height(8.dp))

                FilterOption(
                    onCheckedChange = viewModel::onStateChangeTramited,
                    label = PaymentStatus.TRAMITED.label,
                    value = state.paymentStatusTramited,
                )

                Spacer(modifier = Modifier.height(8.dp))

                FilterOption(
                    onCheckedChange = viewModel::onStateChangeCanceled,
                    label = PaymentStatus.CANCELED.label,
                    value = state.paymentStatusCanceled,
                )

                Spacer(modifier = Modifier.height(8.dp))

                FilterOption(
                    onCheckedChange = viewModel::onStateChangeFixed,
                    label = PaymentStatus.FIXED_PAYMENT.label,
                    value = state.paymentStatusFixed,
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
        }

        // Botones Sticky al final
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(LocalSpacing.current.la)
                .fillMaxWidth()
        ) {
            Button(
                onClick = {
                    applyButtonEnabled = false
                    navController.previousBackStackEntry?.savedStateHandle?.set("filters_result", state)
                    onBack()
                    analytics.logEvent ( "ButtonApplyFilters" ) {
                        param("eventType", "Click")
                    }
                },
                enabled = applyButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E5D4B)),
                shape = RoundedCornerShape(28.dp),
            ) {
                Text(
                    text = stringResource(R.string.fsButtonApply),
                    color = Color.White,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            TextButton(
                onClick = {
                    if (!isProcesing) {
                        viewModel.deleteFilters()
                        analytics.logEvent("ButtonDeleteFilters") {
                            param("eventType", "Click")
                        }
                    }
                },
            ) {
                Text(
                    textDecoration = TextDecoration.Underline,
                    text = stringResource(R.string.fsButtonDelete),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2E5D4B),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewFilterScreen() {
    FilterScreen(navController = rememberNavController(), analytics = FirebaseAnalytics.getInstance(LocalContext.current), onBack = {}, isProcesing = false)
}
