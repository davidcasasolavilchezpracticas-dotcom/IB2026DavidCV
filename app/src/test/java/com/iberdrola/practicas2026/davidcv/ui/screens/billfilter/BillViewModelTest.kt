package com.iberdrola.practicas2026.davidcv.ui.screens.billfilter

import android.content.Context
import app.cash.turbine.test
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class BillViewModelTest {

    private lateinit var viewModel: BillViewModel
    
    @MockK(relaxed = true)
    private lateinit var context: Context
    
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = BillViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be default BillFilterState`() = runTest {
        viewModel.state.test {
            val state = awaitItem()
            assertNull(state.startDate)
            assertNull(state.endDate)
            assertNull(state.priceRange)
            assertFalse(state.paymentStatusPaid)
        }
    }

    @Test
    fun `when onPriceRangeChanged is called, state should be updated`() = runTest {
        // Given
        val range = 10f..100f

        // When
        viewModel.onPriceRangeChanged(range)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(range, state.priceRange)
        }
    }

    @Test
    fun `when onStateChangePaid is called with true, paymentStatusPaid should be true`() = runTest {
        // When
        viewModel.onStateChangePaid(true)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state.paymentStatusPaid)
        }
    }

    @Test
    fun `when deleteFilters is called, state should return to default`() = runTest {
        // Given
        viewModel.onStateChangePaid(true)
        viewModel.onPriceRangeChanged(10f..50f)

        // When
        viewModel.deleteFilters()

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertFalse(state.paymentStatusPaid)
            assertNull(state.priceRange)
        }
    }

    @Test
    fun `when onValidStartDate is called with date after endDate, it should return false`() = runTest {
        // Given - Set end date to 2023-01-01
        // Nota: asumiendo que dfValidateDate es dd/MM/yyyy basado en nombres de strings
        // Para el test usamos fechas que pasen por el parser si conocemos el formato
        // Si no, probamos la lógica de los métodos onValid...
        
        // Simulamos el estado interno para probar la lógica de validación
        viewModel.onEndDateSelected("01/01/2023", context)
        
        // When/Then
        assertFalse(viewModel.onValidStartDate("02/01/2023"))
        assertTrue(viewModel.onValidStartDate("31/12/2022"))
    }
}
