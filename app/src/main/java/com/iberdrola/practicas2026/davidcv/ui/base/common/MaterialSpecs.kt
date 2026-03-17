package com.iberdrola.practicas2026.davidcv.ui.base.common

import android.graphics.Color
import androidx.compose.material3.CardColors
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.ui.theme.Black
import com.iberdrola.practicas2026.davidcv.ui.theme.EnergyGreenLight
import com.iberdrola.practicas2026.davidcv.ui.theme.White
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Locale


//region Espaciadores

data class Spacing(
    val xxs: Dp = 3.dp,
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 12.dp,
    val lg: Dp = 16.dp,
    val la: Dp = 20.dp,
    val xl: Dp = 24.dp,
    val xxl: Dp = 32.dp
)

val LocalSpacing = compositionLocalOf { Spacing() }

//endregion

//region CardColors

val cardColors = CardColors(
    containerColor = White,
    contentColor = Black,
    disabledContainerColor = EnergyGreenLight,
    disabledContentColor = EnergyGreenLight
)

//endregion

//region DateFormatter

val localeEs: Locale = Locale.forLanguageTag("es-ES")
val dfLastBill: DateTimeFormatter? = DateTimeFormatter.ofPattern("dd MMM. yyyy", localeEs)
val dfNormalBill: DateTimeFormatter? = DateTimeFormatter.ofPattern("dd 'de' MMMM", localeEs)
val dfValidateDate: DateTimeFormatter? = DateTimeFormatter.ofPattern("dd-MM-yyyy", localeEs)


//endregion

