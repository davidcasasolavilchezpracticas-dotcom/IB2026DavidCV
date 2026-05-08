package com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.ui.theme.EnergyGreen
import com.iberdrola.practicas2026.davidcv.ui.theme.White

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun ButtonFilter(
    selectedFilters: List<String>,
    icon: ImageVector? = null,
    onFilterClick: () -> Unit,
    onLongClick: () -> Unit,
    filtersCount: Int,
    label: String,
    enabled: Boolean = true, // Nuevo parámetro para controlar interacciones
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val backgroundColor = if (isFocused) EnergyGreen else White
    val contentColor = if (isFocused) White else EnergyGreen

    BadgedBox(
        badge = {
            if (selectedFilters.isNotEmpty()) {
                FilterBadge(count = filtersCount)
            }
        },
        modifier = Modifier.padding(4.dp)
    ) {
        Surface(
            modifier = Modifier
                .clip(RoundedCornerShape(20.dp))
                .combinedClickable(
                    enabled = enabled,
                    interactionSource = interactionSource,
                    indication = LocalIndication.current,
                    onClick = { if (enabled) onFilterClick() },
                    onLongClick = { if (enabled) onLongClick() }
                ),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, EnergyGreen),
            color = backgroundColor,
            contentColor = contentColor,
        ) {
            FilterContent(
                label = label,
                icon = icon,
                color = contentColor
            )
        }
    }
}
