package com.iberdrola.practicas2026.davidcv.ui.screens.contractlist

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.iberdrola.practicas2026.davidcv.domain.model.contract.Contract
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractStatus
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractType
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class ContractListScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun contractListScreen_displaysContractsAndHandlesClick() {
        val mockViewModel = mockk<ContractListViewModel>(relaxed = true)
        val mockRemoteConfig = mockk<FirebaseRemoteConfig>(relaxed = true)
        val mockAnalytics = mockk<FirebaseAnalytics>(relaxed = true)
        
        val contracts = listOf(
            Contract(1, ContractType.LIGHT, ContractStatus.ACTIVE)
        )
        val successState = ContractListState.Success(contracts)
        
        every { mockViewModel.contractsState } returns MutableStateFlow(successState)
        every { mockRemoteConfig.getBoolean("ContractGasAviable") } returns true
        every { mockRemoteConfig.getBoolean("ContractLightAviable") } returns true

        composeTestRule.setContent {
            val navController = rememberNavController()
            ContractListScreen(
                viewModel = mockViewModel,
                navController = navController,
                remoteConfig = mockRemoteConfig,
                analytics = mockAnalytics
            )
        }

        // Verifica que se muestra el contrato de luz usando el label del enum
        composeTestRule.onNodeWithText("Contrato de luz").assertIsDisplayed()
        
        // Verifica el click
        composeTestRule.onNodeWithText("Contrato de luz").performClick()
    }

    @Test
    fun contractListScreen_displaysEmptyState_whenNoContracts() {
        val mockViewModel = mockk<ContractListViewModel>(relaxed = true)
        val mockRemoteConfig = mockk<FirebaseRemoteConfig>(relaxed = true)
        val mockAnalytics = mockk<FirebaseAnalytics>(relaxed = true)
        
        every { mockViewModel.contractsState } returns MutableStateFlow(ContractListState.Success(emptyList()))

        composeTestRule.setContent {
            val navController = rememberNavController()
            ContractListScreen(
                viewModel = mockViewModel,
                navController = navController,
                remoteConfig = mockRemoteConfig,
                analytics = mockAnalytics
            )
        }

        // Verifica que se muestra el componente de lista vacía
        // Usamos el texto de los recursos string: R.string.noAviableContracts -> "No hay contratos disponibles"
        composeTestRule.onNodeWithText("No hay contratos disponibles").assertIsDisplayed()
        
        // También podemos verificar el botón de recargar
        composeTestRule.onNodeWithText("Recargar").assertIsDisplayed()
    }
}
