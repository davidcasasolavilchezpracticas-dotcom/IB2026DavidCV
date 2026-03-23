package com.iberdrola.practicas2026.davidcv.ui.screens.contractlist

import app.cash.turbine.test
import com.iberdrola.practicas2026.davidcv.domain.exception.ContractException
import com.iberdrola.practicas2026.davidcv.domain.model.contract.Contract
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractStatus
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractType
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.usecase.GetContractsUseCase
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

@OptIn(ExperimentalCoroutinesApi::class)
class ContractListViewModelTest {

    @MockK
    private lateinit var getContractsUseCase: GetContractsUseCase

    private lateinit var viewModel: ContractListViewModel
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
    fun `when getContracts is called and returns success, state should be Success`() = runTest {
        // Given
        val contracts = listOf(
            Contract(1, ContractType.LIGHT, ContractStatus.ACTIVE)
        )
        coEvery { getContractsUseCase(any()) } returns flowOf(BaseResult.Success(contracts))

        // When
        viewModel = ContractListViewModel(getContractsUseCase)

        // Then
        viewModel.contractsState.test {
            val state = awaitItem()
            assertTrue(state is ContractListState.Success)
            assertEquals(contracts, (state as ContractListState.Success).contracts)
        }
    }

    @Test
    fun `when getContracts is called and returns error, state should be Error`() = runTest {
        // Given
        val exception = ContractException.ConexionFailed
        coEvery { getContractsUseCase(any()) } returns flowOf(BaseResult.Error(exception))

        // When
        viewModel = ContractListViewModel(getContractsUseCase)

        // Then
        viewModel.contractsState.test {
            val state = awaitItem()
            assertTrue(state is ContractListState.Error)
            assertEquals(exception, (state as ContractListState.Error).exception)
        }
    }
}
