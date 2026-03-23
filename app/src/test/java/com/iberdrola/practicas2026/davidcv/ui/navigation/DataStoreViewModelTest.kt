package com.iberdrola.practicas2026.davidcv.ui.navigation

import app.cash.turbine.test
import com.iberdrola.practicas2026.davidcv.data.local.datastore.DataStoreManager
import com.iberdrola.practicas2026.davidcv.domain.model.account.Account
import io.mockk.MockKAnnotations
import io.mockk.every
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DataStoreViewModelTest {

    @MockK
    private lateinit var dataStoreManager: DataStoreManager

    private lateinit var viewModel: DataStoreViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        
        // Default behavior for mocks
        every { dataStoreManager.account } returns flowOf(null)
        every { dataStoreManager.bsCounter } returns flowOf(0)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial uiState should have default account when datastore is empty`() = runTest {
        // When
        viewModel = DataStoreViewModel(dataStoreManager)

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("Julian", state.name)
            assertEquals("julian@gmail.com", state.email)
            assertTrue(state.isEmailValid)
        }
    }

    @Test
    fun `when name changes, uiState should be updated`() = runTest {
        // Given
        viewModel = DataStoreViewModel(dataStoreManager)

        // When
        viewModel.onNameChange("David")

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("David", state.name)
        }
    }

    @Test
    fun `when invalid email is entered, isEmailValid should be false`() = runTest {
        // Given
        viewModel = DataStoreViewModel(dataStoreManager)

        // When
        viewModel.onEmailChange("invalid-email")

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("invalid-email", state.email)
            assertFalse(state.isEmailValid)
        }
    }

    @Test
    fun `when valid email is entered, isEmailValid should be true`() = runTest {
        // Given
        viewModel = DataStoreViewModel(dataStoreManager)

        // When
        viewModel.onEmailChange("test@example.com")

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("test@example.com", state.email)
            assertTrue(state.isEmailValid)
        }
    }
}
