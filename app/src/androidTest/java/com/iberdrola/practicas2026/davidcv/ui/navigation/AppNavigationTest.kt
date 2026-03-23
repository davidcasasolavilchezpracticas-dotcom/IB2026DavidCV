package com.iberdrola.practicas2026.davidcv.ui.navigation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextReplacement
import com.iberdrola.practicas2026.davidcv.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AppNavigationTest {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun full_edit_profile_flow() {
        // 1. Ir a la pantalla de cuenta
        // Nota: Asegúrate de que el ContentDescription coincida con el de tu UI
        composeTestRule.onNodeWithText("Modificar datos", substring = true).assertIsDisplayed()
        
        // 2. Click en Editar (Modificar datos)
        composeTestRule.onNodeWithText("Modificar datos").performClick()

        // 3. Cambiar el nombre
        composeTestRule.onNodeWithText("Nombre").performTextReplacement("Nuevo Nombre Test")

        // 4. Click en Guardar
        composeTestRule.onNodeWithText("Guardar cambios").performClick()

        // 5. Verificar que el cambio se refleja (vuelve a la pantalla anterior)
        composeTestRule.onNodeWithText("Nuevo Nombre Test").assertIsDisplayed()
    }

    @Test
    fun full_filter_flow() {
        // 1. Navegar a facturas
        composeTestRule.onNodeWithText("Ver facturas", substring = true).performClick()

        // 2. Abrir Filtro
        composeTestRule.onNodeWithText("Filtrar").performClick()

        // 3. Seleccionar un filtro
        composeTestRule.onNodeWithText("Pagada", substring = true).performClick()

        // 4. Aplicar Filtros
        composeTestRule.onNodeWithText("Aplicar Filtros").performClick()

        // 5. Verificar que volvemos al listado
        composeTestRule.onNodeWithText("Mis facturas").assertIsDisplayed()
    }
}
