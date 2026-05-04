package com.iberdrola.practicas2026.davidcv.ui.base.composables.billfilter

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.theme.EnergyGreen
import com.iberdrola.practicas2026.davidcv.ui.theme.EnergyGreenLight


/**
 * PriceRangeSelector
 * Componente que permite seleccionar un rango de precios mediante un RangeSlider de Material 3
 *
 * @param selectedRange Rango de precios seleccionado
 * @param totalRange Rango de precios total
 * @param onSliderChange Callback cuando se cambia el valor
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriceRangeSelector(
    selectedRange: ClosedFloatingPointRange<Float>,
    totalRange: ClosedFloatingPointRange<Float>,
    onSliderChange: (ClosedFloatingPointRange<Float>) -> Unit
) {
    var sliderPosition by remember(selectedRange) { mutableStateOf(selectedRange) }
    val minGap = 1f

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.tituloPriceRangeSelector),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .background(Color(0xFFE0E8E3), RoundedCornerShape(4.dp))
                .padding(horizontal = LocalSpacing.current.md, vertical = LocalSpacing.current.xs)
        ) {
            Text(
                text = "${sliderPosition.start.toInt()} € - ${sliderPosition.endInclusive.toInt()} €",
                fontSize = 12.sp,
                fontWeight = FontWeight.W600,
                color = Color.Black
            )
        }

        RangeSlider(
            value = sliderPosition,
            onValueChange = { newValues ->
                // Comprobamos si se está respetando la distancia mínima
                sliderPosition = if (newValues.endInclusive - newValues.start >= minGap) {
                    newValues
                } else {
                    if (newValues.start != sliderPosition.start) {
                        (newValues.endInclusive - minGap)..newValues.endInclusive
                    } else {
                        newValues.start..(newValues.start + minGap)
                    }
                }
                onSliderChange(sliderPosition)
            },
            valueRange = totalRange,
            startThumb = {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(EnergyGreen, CircleShape)
                )
            },
            endThumb = {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(EnergyGreen, CircleShape)
                )
            },
            track = { rangeSliderState ->
                SliderDefaults.Track(
                    rangeSliderState = rangeSliderState,
                    modifier = Modifier.height(4.dp),
                    colors = SliderDefaults.colors(
                        activeTrackColor = EnergyGreen,
                        inactiveTrackColor = EnergyGreenLight
                    ),
                    thumbTrackGapSize = 0.dp
                )
            }
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "${totalRange.start.toInt()} €", color = Color.Gray, fontSize = 12.sp)
            Text(text = "${totalRange.endInclusive.toInt()} €", color = Color.Gray, fontSize = 12.sp)
        }
    }
}
