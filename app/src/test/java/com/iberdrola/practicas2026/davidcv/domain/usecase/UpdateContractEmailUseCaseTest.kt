package com.iberdrola.practicas2026.davidcv.domain.usecase

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

class UpdateContractEmailUseCaseTest {

    @MockK
    private lateinit var repository: ContractRepositoryInterface

    private lateinit var updateContractEmailUseCase: UpdateContractEmailUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        updateContractEmailUseCase = UpdateContractEmailUseCase(repository)
    }

    @Test
    fun `invoke should call repository updateContractEmail and return result`() = runTest {
        // Given
        val id = 1
        val email = "new@example.com"
        val expectedResult = BaseResult.Success(Unit)
        coEvery { repository.updateContractEmail(id, email) } returns expectedResult

        // When
        val result = updateContractEmailUseCase(id, email)

        // Then
        assertEquals(expectedResult, result)
        coVerify { repository.updateContractEmail(id, email) }
    }
}
