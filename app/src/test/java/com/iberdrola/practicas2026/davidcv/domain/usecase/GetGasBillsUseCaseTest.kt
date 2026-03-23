package com.iberdrola.practicas2026.davidcv.domain.usecase

import app.cash.turbine.test
import com.iberdrola.practicas2026.davidcv.domain.model.bill.Bill
import com.iberdrola.practicas2026.davidcv.domain.model.bill.BillType
import com.iberdrola.practicas2026.davidcv.domain.model.bill.PaymentStatus
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.repository.BillRepositoryInterface
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class GetGasBillsUseCaseTest {

    @MockK
    private lateinit var repository: BillRepositoryInterface

    private lateinit var getGasBillsUseCase: GetGasBillsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        getGasBillsUseCase = GetGasBillsUseCase(repository)
    }

    @Test
    fun `invoke should call repository getBillsByType with GAS and return results`() = runTest {
        // Given
        val bills = listOf(
            Bill(1, BillType.GAS, 45.0f, LocalDateTime.now(), LocalDateTime.now(), PaymentStatus.PAID)
        )
        val expectedResult = BaseResult.Success(bills)
        coEvery { repository.getBillsByType(BillType.GAS) } returns flowOf(expectedResult)

        // When
        getGasBillsUseCase().test {
            // Then
            assertEquals(expectedResult, awaitItem())
            awaitComplete()
        }

        coVerify { repository.getBillsByType(BillType.GAS) }
    }
}
