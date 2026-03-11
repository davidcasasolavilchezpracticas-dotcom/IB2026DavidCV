package com.iberdrola.practicas2026.davidcv.ui.base.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SentimentNeutral
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material.icons.filled.SentimentVerySatisfied
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SatisfactionPicker(
    modifier: Modifier = Modifier,
    onRatingSelected: (Int) -> Unit
) {
    val icons = listOf(
        Icons.Default.SentimentVeryDissatisfied to Color(0xFFE53935),
        Icons.Default.SentimentDissatisfied to Color(0xFFFFB300),
        Icons.Default.SentimentNeutral to Color(0xFF9E9E9E),
        Icons.Default.SentimentSatisfied to Color(0xFF039BE5),
        Icons.Default.SentimentVerySatisfied to Color(0xFF2E7D32)
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        icons.forEachIndexed { index, (icon, color) ->
            IconButton(onClick = { onRatingSelected(index) }) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}