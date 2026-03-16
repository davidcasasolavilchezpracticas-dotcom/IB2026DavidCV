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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PriceRangeSelector(range: ClosedFloatingPointRange<Float>, onSliderChange: (ClosedFloatingPointRange<Float>) -> Unit) {
    var sliderPosition by remember { mutableStateOf(range) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Por un importe", fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .background(Color(0xFFE0E8E3), RoundedCornerShape(4.dp))
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = "${sliderPosition.start.toInt()} € - ${sliderPosition.endInclusive.toInt()} €",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E4D3E)
            )
        }

        RangeSlider(
            value = sliderPosition,
            onValueChange = { pos ->  onSliderChange(pos) },
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = Color(0xFF006633),
                activeTrackColor = Color(0xFF006633)
            )
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "${range.start} €", color = Color.Gray, fontSize = 12.sp)
            Text(text = "${range.endInclusive} €", color = Color.Gray, fontSize = 12.sp)
        }
    }
}