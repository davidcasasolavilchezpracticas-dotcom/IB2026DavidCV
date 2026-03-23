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

class UpdateContractPhoneUseCaseTest {

    @MockK
    private lateinit var repository: ContractRepositoryInterface

    private lateinit var updateContractPhoneUseCase: UpdateContractPhoneUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        updateContractPhoneUseCase = UpdateContractPhoneUseCase(repository)
    }

    @Test
    fun `invoke should call repository updateContractPhone and return result`() = runTest {
        // Given
        val id = 1
        val phone = "600000000"
        val expectedResult = BaseResult.Success(Unit)
        coEvery { repository.updateContractPhone(id, phone) } returns expectedResult

        // When
        val result = updateContractPhoneUseCase(id, phone)

        // Then
        assertEquals(expectedResult, result)
        coVerify { repository.updateContractPhone(id, phone) }
    }
}
