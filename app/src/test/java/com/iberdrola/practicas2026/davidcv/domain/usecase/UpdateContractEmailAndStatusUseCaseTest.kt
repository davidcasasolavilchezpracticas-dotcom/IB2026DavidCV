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

class UpdateContractEmailAndStatusUseCaseTest {

    @MockK
    private lateinit var repository: ContractRepositoryInterface

    private lateinit var updateUseCase: UpdateContractEmailAndStatusUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        updateUseCase = UpdateContractEmailAndStatusUseCase(repository)
    }

    @Test
    fun `invoke should call repository updateContractEmail with email and status`() = runTest {
        // Given
        val id = 1
        val email = "test@test.com"
        val status = ContractStatus.ACTIVE
        val expectedResult = BaseResult.Success(Unit)
        coEvery { repository.updateContractEmail(id, email, status) } returns expectedResult

        // When
        val result = updateUseCase(id, email, status)

        // Then
        assertEquals(expectedResult, result)
        coVerify { repository.updateContractEmail(id, email, status) }
    }
}
