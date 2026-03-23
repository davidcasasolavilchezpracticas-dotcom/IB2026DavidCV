package com.iberdrola.practicas2026.davidcv.ui.screens.useraccount

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import com.iberdrola.practicas2026.davidcv.domain.model.account.Account
import com.iberdrola.practicas2026.davidcv.ui.navigation.DataStoreViewModel
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test

class UserAccountScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun userAccountScreen_displaysUserInfo() {
        val mockViewModel = mockk<DataStoreViewModel>(relaxed = true)
        val testAccount = Account(1, "Test User", "test@test.com", null)
        val uiState = UserAccountState(
            isLoading = false, 
            account = testAccount, 
            name = "Test User", 
            email = "test@test.com", 
            profileImage = null
        )
        
        every { mockViewModel.uiState } returns MutableStateFlow(uiState)

        composeTestRule.setContent {
            val navController = rememberNavController()
            UserAccountScreen(
                viewModel = mockViewModel,
                navController = navController,
                analytics = mockk()
            )
        }

        composeTestRule.onNodeWithText("Test User").assertIsDisplayed()
        composeTestRule.onNodeWithText("test@test.com").assertIsDisplayed()
    }
}
