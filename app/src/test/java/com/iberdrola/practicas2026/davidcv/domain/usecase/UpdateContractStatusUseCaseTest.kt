package com.iberdrola.practicas2026.davidcv.domain.usecase

import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractStatus
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.repository.ContractRepositoryInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class UpdateContractStatusUseCaseTest {

    @MockK
    private lateinit var repository: ContractRepositoryInterface

    private lateinit var updateContractStatusUseCase: UpdateContractStatusUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        updateContractStatusUseCase = UpdateContractStatusUseCase(repository)
    }

    @Test
    fun `invoke should call repository updateContractStatus and return result`() = runTest {
        // Given
        val id = 1
        val status = ContractStatus.INACTIVE
        val expectedResult = BaseResult.Success(Unit)
        coEvery { repository.updateContractStatus(id, status) } returns expectedResult

        // When
        val result = updateContractStatusUseCase(id, status)

        // Then
        assertEquals(expectedResult, result)
        coVerify { repository.updateContractStatus(id, status) }
    }
}
