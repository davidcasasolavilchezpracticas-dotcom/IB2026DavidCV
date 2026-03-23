package com.iberdrola.practicas2026.davidcv.domain.usecase

import app.cash.turbine.test
import com.iberdrola.practicas2026.davidcv.domain.model.contract.Contract
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractStatus
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractType
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.repository.ContractRepositoryInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetContractByIdUseCaseTest {

    @MockK
    private lateinit var repository: ContractRepositoryInterface

    private lateinit var getContractByIdUseCase: GetContractByIdUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getContractByIdUseCase = GetContractByIdUseCase(repository)
    }

    @Test
    fun `when id exists, should return Success with the contract`() = runTest {
        // Given
        val contract1 = Contract(1, ContractType.LIGHT, ContractStatus.ACTIVE)
        val contract2 = Contract(2, ContractType.GAS, ContractStatus.INACTIVE)
        val contracts = listOf(contract1, contract2)
        
        coEvery { repository.getContracts(any()) } returns flowOf(BaseResult.Success(contracts))

        // When
        getContractByIdUseCase(1).test {
            // Then
            val result = awaitItem()
            assertTrue(result is BaseResult.Success)
            assertEquals(contract1, (result as BaseResult.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `when id does not exist, should return Error`() = runTest {
        // Given
        val contracts = listOf(Contract(1, ContractType.LIGHT, ContractStatus.ACTIVE))
        coEvery { repository.getContracts(any()) } returns flowOf(BaseResult.Success(contracts))

        // When
        getContractByIdUseCase(99).test {
            // Then
            val result = awaitItem()
            assertTrue(result is BaseResult.Error)
            assertEquals("Contrato no encontrado", (result as BaseResult.Error).exception.message)
            awaitComplete()
        }
    }
}
