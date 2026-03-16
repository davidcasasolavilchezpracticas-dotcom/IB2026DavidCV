package com.iberdrola.practicas2026.davidcv.ui.base.composables.billfilter

import android.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * DateSelector
 * Componente que permite seleccionar una fecha mediante un DatePicker de Material 3
 *
 * @param label Etiqueta para el campo de texto
 * @param modifier Modificador de Compose
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelector(label: String, modifier: Modifier = Modifier, onConfirm: (String) -> Unit, onValidDate: (String) -> Boolean) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedDateText by remember { mutableStateOf("") }
    val datePickerState = rememberDatePickerState()

    // Formateador de fecha
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        var auxText: String = ""
                        datePickerState.selectedDateMillis?.let { millis ->
                            auxText = formatter.format(Date(millis))
                        }
                        if (onValidDate(auxText))
                        {
                            selectedDateText =  auxText
                        }
                        onConfirm(selectedDateText)
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    OutlinedTextField(
        value = selectedDateText,
        onValueChange = {},
        label = { Text("* $label") },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = null,
                modifier = Modifier.clickable { showDialog = true }
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .clickable { showDialog = true },
        readOnly = true,
        enabled = false, // Para que el clic funcione en todo el componente
        colors = TextFieldDefaults.colors(
            disabledTextColor = Color.Black,
            disabledContainerColor = Color.Transparent,
            disabledIndicatorColor = Color.Gray,
            disabledLabelColor = Color.Gray,
            disabledTrailingIconColor = Color.Gray
        )
    )
}
