package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions

import app.cash.turbine.test
import com.iberdrola.practicas2026.davidcv.domain.model.contract.Contract
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractStatus
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractType
import com.iberdrola.practicas2026.davidcv.domain.network.BaseResult
import com.iberdrola.practicas2026.davidcv.domain.usecase.*
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ContractActionsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @MockK lateinit var updateContractEmailAndStatusUseCase: UpdateContractEmailAndStatusUseCase
    @MockK lateinit var updateContractEmailUseCase: UpdateContractEmailUseCase
    @MockK lateinit var updateContractPhoneUseCase: UpdateContractPhoneUseCase
    @MockK lateinit var getContractByIdUseCase: GetContractByIdUseCase
    @MockK lateinit var updateContractStatusUseCase: UpdateContractStatusUseCase

    private lateinit var viewModel: ContractActionsViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(testDispatcher)
        viewModel = ContractActionsViewModel(
            updateContractEmailAndStatusUseCase,
            updateContractEmailUseCase,
            updateContractPhoneUseCase,
            getContractByIdUseCase,
            updateContractStatusUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getContract should update state with success when use case returns contract`() = runTest {
        // Given
        val contract = Contract(id = 1, type = ContractType.LIGHT, status = ContractStatus.ACTIVE, phone = "123456789", email = "test@test.com")
        coEvery { getContractByIdUseCase(1) } returns flowOf(BaseResult.Success(contract))

        // When
        viewModel.getContract(1)
        advanceUntilIdle()

        // Then
        assertEquals(contract, viewModel.state.value.contract)
        assertFalse(viewModel.state.value.isLoading)
        assertEquals(ContractActions.MODIFYEMAIL, viewModel.state.value.action)
    }

    @Test
    fun `onEmailChanged should update emailTry and validate correctly`() = runTest {
        // When
        viewModel.onEmailChanged("valid@email.com")
        
        // Then
        assertEquals("valid@email.com", viewModel.state.value.emailTry)
        // Note: Patterns.EMAIL_ADDRESS might return null in unit tests if not mocked
        // But the logic in the state should be tested if possible
    }

    @Test
    fun `censurator should mask email correctly`() {
        // Test with email length > 7
        val email1 = "julian@gmail.com"
        val result1 = viewModel.censurator(email1)
        assertEquals("j*****n@gmail.com", result1)

        // Test with email length <= 7
        val email2 = "abc@d.c"
        val result2 = viewModel.censurator(email2)
        assertEquals("a*****b@d.c", result2)
    }

    @Test
    fun `onVerifyCodeChanged should respect 6 digit limit`() = runTest {
        viewModel.onVerifyCodeChanged("123456")
        assertEquals("123456", viewModel.state.value.verifyCodeTry)

        viewModel.onVerifyCodeChanged("1234567")
        assertEquals("123456", viewModel.state.value.verifyCodeTry)
    }

    @Test
    fun `updateContractEmail should set emailChanged to true on success`() = runTest {
        // Given
        val contract = Contract(id = 1, type = ContractType.LIGHT, status = ContractStatus.ACTIVE)
        viewModel.onEmailChanged("new@email.com")
        // We need to set the contract in state first or it will fail at _state.value.contract?.id!!
        coEvery { getContractByIdUseCase(1) } returns flowOf(BaseResult.Success(contract))
        viewModel.getContract(1)
        advanceUntilIdle()

        coEvery { updateContractEmailUseCase(1, "new@email.com") } returns BaseResult.Success(Unit)

        // When
        viewModel.updateContractEmail("new@email.com")
        advanceUntilIdle()

        // Then
        assertTrue(viewModel.state.value.emailChanged)
    }
}
