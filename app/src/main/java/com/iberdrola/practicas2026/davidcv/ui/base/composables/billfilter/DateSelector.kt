import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.SelectableDates
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
import androidx.compose.ui.platform.LocalLocale
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
import java.time.ZoneOffset
import java.util.Date

@SuppressLint("NonObservableLocale")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelector(
    modifier: Modifier = Modifier,
    onConfirm: (String) -> Unit,
    minDate: LocalDateTime?,
    maxDate: LocalDateTime?,
    enabled: Boolean = true,
    date: LocalDateTime?,
    label: String,
) {
    var showDialog by remember { mutableStateOf(false) }

    val selectableDates = remember(minDate, maxDate) {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val minMillis = minDate?.atZone(ZoneOffset.UTC)?.toInstant()?.toEpochMilli() ?: Long.MIN_VALUE
                val maxMillis = maxDate?.atZone(ZoneOffset.UTC)?.toInstant()?.toEpochMilli() ?: Long.MAX_VALUE
                return utcTimeMillis in minMillis..maxMillis
            }

            override fun isSelectableYear(year: Int): Boolean {
                return year >= (minDate?.year ?: 0) && year <= (maxDate?.year ?: 3000)
            }
        }
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = minDate?.plusDays(1)?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli(),
        initialDisplayMode = DisplayMode.Picker,
        yearRange = (minDate?.year ?: 2000)..(maxDate?.year ?: LocalDateTime.now().year),
        selectableDates = selectableDates
    )

    val formatter = SimpleDateFormat("dd-MM-yyyy", LocalLocale.current.platformLocale)
    val selectedDateText = date?.let {
        formatter.format(Date.from(it.atZone(ZoneId.systemDefault()).toInstant()))
    } ?: ""

    if (showDialog && enabled) {
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
            colors = DatePickerDefaults.colors(containerColor = Color.White),
            shape = RoundedCornerShape(28.dp),
            modifier = Modifier.clip(RoundedCornerShape(28.dp))
        ) {
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

            DatePicker(
                state = datePickerState,
                title = null,
                headline = null,
                showModeToggle = false,
                colors = DatePickerDefaults.colors(
                    containerColor = Color.White,
                    weekdayContentColor = Color.Gray,
                    dayContentColor = Color.Black,
                    selectedDayContainerColor = EnergyGreen,
                    selectedDayContentColor = Color.White,
                    todayContentColor = EnergyGreen,
                    todayDateBorderColor = EnergyGreen,
                    navigationContentColor = Color.Black
                )
            )
        }
    }

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
            .height(64.dp)
            .fillMaxWidth()
            .clickable { showDialog = true },
        readOnly = true,
        enabled = false,
        colors = TextFieldDefaults.colors(
            disabledTextColor = Color.Black,
            disabledContainerColor = Color.White,
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
        date = LocalDateTime.now(),
        label = "Fecha",
        onConfirm = {},
        minDate = null,
        maxDate = null,
    )
}
