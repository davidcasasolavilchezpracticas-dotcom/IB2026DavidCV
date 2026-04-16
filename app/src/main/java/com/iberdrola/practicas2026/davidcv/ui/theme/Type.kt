package com.iberdrola.practicas2026.davidcv.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.davidcv.R


val IberPangeaFont = FontFamily (
    Font(
        resId = R.font.iber_pangea,
        weight = FontWeight.Normal
    ),
    Font(
        resId = R.font.pangea_bold,
        weight = FontWeight.Bold
    ),
)


val Typography = Typography(
    // Para el monto grande de la factura principal (20,00 €)
    headlineLarge = TextStyle(
        fontFamily = IberPangeaFont,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        letterSpacing = (-0.5).sp
    ),
    // Para títulos de sección como "Mis facturas"
    headlineSmall = TextStyle(
        fontFamily = IberPangeaFont,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        letterSpacing = 0.sp
    ),
    // Para encabezados secundarios como "Histórico de facturas"
    titleLarge = TextStyle(
        fontFamily = IberPangeaFont,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    // Para la fecha en la lista de facturas
    titleMedium = TextStyle(
        fontFamily = IberPangeaFont,
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        fontWeight = FontWeight.ExtraBold,
        fontSize = 16.sp
    ),
    // Para "Última factura" dentro de la tarjeta
    titleSmall = TextStyle(
        fontFamily = IberPangeaFont,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp
    ),
    // Para la dirección (C/ PALMA...)
    bodyLarge = TextStyle(
        fontFamily = IberPangeaFont,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    // Para texto estándar y periodos de facturación
    bodyMedium = TextStyle(
        fontFamily = IberPangeaFont,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        color = Color.Gray // Opcional, mejor manejarlo en el Composable
    ),
    // Para el texto de "Factura Luz" debajo de la fecha
    bodySmall = TextStyle(
        fontFamily = IberPangeaFont,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    // Para los estados (Pagada / Pendiente) y texto del botón "Filtrar"
    labelMedium = TextStyle(
        fontFamily = IberPangeaFont,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        letterSpacing = 0.5.sp
    )
)

