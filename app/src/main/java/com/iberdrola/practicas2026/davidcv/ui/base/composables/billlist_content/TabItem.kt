package com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.ui.theme.EnergyGreen

/**
 * TabItem
 * Muestra el tab que gestiona la muestra de pantalla
 *
 * @param text Texto del tab
 * @param isSelected Indica si el tab está seleccionado
 * @param onClick Función a ejecutar al hacer clic en el tab
 */
@Composable
fun TabItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    Text(
        text = text,
        color = if (isSelected) Color.Black else Color.Gray,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .drawBehind {
                if (isSelected) {
                    val strokeWidth = 5.dp.toPx()
                    val y = size.height + 6.dp.toPx()

                    // Define cuánto quieres que sobresalga la línea por cada lado
                    val horizontalExpansion = 8.dp.toPx()

                    drawLine(
                        color = EnergyGreen,
                        start = Offset(-horizontalExpansion, y),
                        end = Offset(size.width + horizontalExpansion, y),
                        strokeWidth = strokeWidth,
                    )
                }
            }
    )
}

@Composable
@Preview
fun TabItemPreview() {
    TabItem(text = "Tab 1", isSelected = true) {}
}