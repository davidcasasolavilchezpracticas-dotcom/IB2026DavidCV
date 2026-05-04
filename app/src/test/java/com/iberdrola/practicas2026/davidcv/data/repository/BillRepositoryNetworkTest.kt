package com.iberdrola.practicas2026.davidcv.data.repository

import app.cash.turbine.test
import com.iberdrola.practicas2026.davidcv.data.remote.retrofit.ApiService
import com.iberdrola.practicas2026.davidcv.domain.exception.BillException
import com.iberdrola.practicas2026.davidcv.domain.model.bill.BillType
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class BillRepositoryNetworkTest {

    @MockK
    private lateinit var apiService: ApiService

    private lateinit var repository: BillRepositoryNetwork

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = BillRepositoryNetwork(apiService)
    }

    @Test
    fun `getBillsByType should emit Success when API call is successful`() = runTest {
        // Given
        coEvery { apiService.getBills() } returns Response.success(emptyList())

        // When
        repository.getBillsByType(BillType.LIGHT).test {
            // Then
            val item = awaitItem()
            assertTrue(item is BaseResult.Success)
            awaitComplete()
        }
    }

    @Test
    fun `getBillsByType should emit ConexionFailed when IOException occurs`() = runTest {
        // Given
        coEvery { apiService.getBills() } throws IOException()

        // When
        repository.getBillsByType(BillType.GAS).test {
            // Then
            val item = awaitItem()
            assertTrue(item is BaseResult.Error && item.exception is BillException.ConexionFailed)
            awaitComplete()
        }
    }

    @Test
    fun `getBillsByType should emit ResponseError when API returns error code`() = runTest {
        // Given
        coEvery { apiService.getBills() } returns Response.error(500, "".toResponseBody())

        // When
        repository.getBillsByType(BillType.LIGHT).test {
            // Then
            val item = awaitItem()
            assertTrue(item is BaseResult.Error && item.exception is BillException.ResponseError)
            awaitComplete()
        }
    }
}
