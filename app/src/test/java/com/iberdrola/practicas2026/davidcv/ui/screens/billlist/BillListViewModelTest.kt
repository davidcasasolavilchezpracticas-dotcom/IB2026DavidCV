package com.iberdrola.practicas2026.davidcv.ui.screens.billlist

import app.cash.turbine.test
import com.iberdrola.practicas2026.davidcv.domain.exception.BillException
import com.iberdrola.practicas2026.davidcv.domain.model.bill.Bill
import com.iberdrola.practicas2026.davidcv.domain.model.bill.BillType
import com.iberdrola.practicas2026.davidcv.domain.model.bill.PaymentStatus
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.usecase.GetGasBillsUseCase
import com.iberdrola.practicas2026.davidcv.domain.usecase.GetLightBillsUseCase
import com.iberdrola.practicas2026.davidcv.ui.screens.billfilter.BillFilterState
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class BillListViewModelTest {

    @MockK
    private lateinit var getLightBillsUseCase: GetLightBillsUseCase
    @MockK
    private lateinit var getGasBillsUseCase: GetGasBillsUseCase

    private lateinit var viewModel: BillListViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when getLightBills returns success, lightBillsState should be Success`() = runTest {
        // Given
        val bills = listOf(
            Bill(1, BillType.LIGHT, 50.0f, LocalDateTime.now(), LocalDateTime.now(), PaymentStatus.PAID)
        )
        coEvery { getLightBillsUseCase() } returns flowOf(BaseResult.Success(bills))

        // When
        viewModel = BillListViewModel(getLightBillsUseCase, getGasBillsUseCase)
        viewModel.getLightBills()

        // Then
        viewModel.lightBillsState.test {
            val state = expectMostRecentItem()
            assertTrue(state is BillListState.Success)
            assertEquals(bills, (state as BillListState.Success).bills)
        }
    }

    @Test
    fun `when getGasBills returns error, gasBillsState should be Error`() = runTest {
        // Given
        val exception = BillException.UnknownError("Error")
        coEvery { getGasBillsUseCase() } returns flowOf(BaseResult.Error(exception))

        // When
        viewModel = BillListViewModel(getLightBillsUseCase, getGasBillsUseCase)
        viewModel.getGasBills()

        // Then
        viewModel.gasBillsState.test {
            val state = expectMostRecentItem()
            assertTrue(state is BillListState.Error)
            assertEquals(exception, (state as BillListState.Error).exception)
        }
    }

    @Test
    fun `when applyFilters is called, bills should be filtered by status`() = runTest {
        // Given
        val bills = listOf(
            Bill(1, BillType.LIGHT, 50.0f, LocalDateTime.now(), LocalDateTime.now(), PaymentStatus.PAID),
            Bill(2, BillType.LIGHT, 30.0f, LocalDateTime.now(), LocalDateTime.now(), PaymentStatus.PENDING)
        )
        coEvery { getLightBillsUseCase() } returns flowOf(BaseResult.Success(bills))
        
        viewModel = BillListViewModel(getLightBillsUseCase, getGasBillsUseCase)
        viewModel.getLightBills()

        // When - Filter only PAID
        val filters = BillFilterState(paymentStatusPaid = true)
        viewModel.applyFilters(filters)

        // Then
        viewModel.lightBillsState.test {
            val state = expectMostRecentItem()
            assertTrue(state is BillListState.Success)
            val filteredList = (state as BillListState.Success).bills
            assertEquals(1, filteredList.size)
            assertEquals(PaymentStatus.PAID, filteredList[0].paymentStatus)
        }
    }
}
