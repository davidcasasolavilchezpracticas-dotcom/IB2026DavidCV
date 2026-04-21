package com.iberdrola.practicas2026.davidcv.ui.base.composables.contractactivate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.ui.theme.EnergyGreen

@Composable
fun ProgressBar(
    progress: Float
){
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = Modifier
            .height(4.dp)
            .background(color = Color(0xFFE0E8E3))
            .fillMaxWidth(),
    ){
        Box(
            contentAlignment = Alignment.TopStart,
            modifier = Modifier
                .background(color = EnergyGreen)
                .fillMaxHeight()
                .fillMaxWidth(progress),
        ){}
    }

}