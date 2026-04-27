import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.theme.EnergyGreen
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelector(
    label: String,
    date: LocalDateTime?,
    modifier: Modifier = Modifier,
    onConfirm: (String) -> Unit,
    onValidDate: (String) -> Boolean,
) {
    var showDialog by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialDisplayMode = DisplayMode.Picker,
    )

    // Formateador de fecha
    val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val selectedDateText = date?.let {
        formatter.format(Date.from(it.atZone(ZoneId.systemDefault()).toInstant()))
    } ?: ""

    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate = formatter.format(Date(millis))
                        onConfirm(selectedDate)
                    }
                    showDialog = false
                }) {
                    Text(text = stringResource(R.string.confirm), color = EnergyGreen, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(text = stringResource(R.string.cancel), color = EnergyGreen)
                }
            },
            // Personalizamos el contenedor del diálogo
            colors = DatePickerDefaults.colors(
                containerColor = Color.White // El fondo base del diálogo será blanco
            ),
            shape = RoundedCornerShape(28.dp),
            modifier = Modifier.clip(RoundedCornerShape(28.dp))
        ) {
            // CABECERA VERDE
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(EnergyGreen)
                    .padding(vertical = 24.dp, horizontal = 24.dp)
            ) {
                Text(
                    text = stringResource(R.string.dateSelector).uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            // CALENDARIO (ZONA BLANCA)
            DatePicker(
                state = datePickerState,
                title = null, // Ya lo pusimos en el Box de arriba
                headline = null, // Quitamos la fecha enorme para que no pise la zona blanca
                showModeToggle = false,
                colors = DatePickerDefaults.colors(
                    containerColor = Color.White, // Aseguramos blanco para que cuadren los días
                    weekdayContentColor = Color.Gray,
                    dayContentColor = Color.Black,
                    selectedDayContainerColor = EnergyGreen,
                    selectedDayContentColor = Color.White,
                    todayContentColor = EnergyGreen,
                    todayDateBorderColor = EnergyGreen,
                    navigationContentColor = Color.Black // Flechas y mes en negro
                )
            )
        }
    }

    // Tu TextField se mantiene igual...
    TextField(
        value = selectedDateText,
        onValueChange = {},
        label = { Text(text = "* $label", fontSize = 14.sp) },
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
            disabledTrailingIconColor = Color.Gray
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