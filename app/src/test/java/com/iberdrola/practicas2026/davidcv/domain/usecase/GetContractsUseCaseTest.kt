package com.iberdrola.practicas2026.davidcv.domain.usecase

import app.cash.turbine.test
import com.iberdrola.practicas2026.davidcv.domain.model.contract.Contract
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractStatus
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractType
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.repository.ContractRepositoryInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetContractsUseCaseTest {

    @MockK
    private lateinit var repository: ContractRepositoryInterface

    private lateinit var getContractsUseCase: GetContractsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getContractsUseCase = GetContractsUseCase(repository)
    }

    @Test
    fun `invoke should call repository getContracts and return its result`() = runTest {
        // Given
        val contracts = listOf(Contract(1, ContractType.LIGHT, ContractStatus.ACTIVE))
        val expectedResult = BaseResult.Success(contracts)
        coEvery { repository.getContracts(any()) } returns flowOf(expectedResult)

        // When
        getContractsUseCase(forceRefresh = true).test {
            // Then
            assertEquals(expectedResult, awaitItem())
            awaitComplete()
        }

        coVerify { repository.getContracts(true) }
    }
}
