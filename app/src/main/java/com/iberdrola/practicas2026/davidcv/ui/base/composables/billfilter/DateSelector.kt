package com.iberdrola.practicas2026.davidcv.ui.base.composables.billfilter

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.iberdrola.practicas2026.davidcv.R
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale

/**
 * DateSelector
 * Componente que permite seleccionar una fecha mediante un DatePicker de Material 3
 *
 * @param label Etiqueta para el campo de texto
 * @param date Fecha actual (LocalDateTime) para mostrar en el campo
 * @param modifier Modificador de Compose
 * @param onConfirm Callback cuando se confirma una fecha
 * @param onValidDate Callback para validar si la fecha seleccionada es permitida
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelector(
    label: String,
    date: LocalDateTime?,
    modifier: Modifier = Modifier,
    onConfirm: (String) -> Unit,
    onValidDate: (String) -> Boolean
) {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    // Formateador de fecha
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val selectedDateText = date?.let {
        formatter.format(Date.from(it.atZone(ZoneId.systemDefault()).toInstant()))
    } ?: ""

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val auxText = formatter.format(Date(millis))
                            if (onValidDate(auxText)) {
                                onConfirm(auxText)
                            }
                        }
                        showDialog = false
                    }) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    TextField(
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
        enabled = false,
        colors = TextFieldDefaults.colors(
            disabledTextColor = Color.Black,
            disabledContainerColor = Color.Transparent,
            disabledIndicatorColor = Color.Gray,
            disabledLabelColor = Color.Gray,
            disabledTrailingIconColor = Color.Gray,
            unfocusedIndicatorColor = Color.Gray,
            focusedIndicatorColor = Color.Gray
        )
    )
}

@Preview
@Composable
fun pwDateSelector(){
    DateSelector(
        label = "Fecha",
        date = LocalDateTime.now(),
        onConfirm = {},
        onValidDate = {true}
    )
}
