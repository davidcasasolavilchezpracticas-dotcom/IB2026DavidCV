package com.iberdrola.practicas2026.davidcv.ui.base.composables.contractlist


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.model.contract.Contract
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content.StatusBadge
import com.iberdrola.practicas2026.davidcv.ui.theme.Black
import com.iberdrola.practicas2026.davidcv.ui.theme.Disabled
import com.iberdrola.practicas2026.davidcv.ui.theme.DisabledIcon
import com.iberdrola.practicas2026.davidcv.ui.theme.IconGreen
import com.iberdrola.practicas2026.davidcv.ui.theme.White

@Composable
fun ContractItem(
    contract: Contract,
    active: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                enabled = active,
                onClick = onClick
            )
    ) {
        Row(
            modifier = Modifier
                .padding(
                    vertical = LocalSpacing.current.lg,
                    horizontal = LocalSpacing.current.xs
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono del servicio
            Icon(
                imageVector = contract.type.icon,
                contentDescription = null,
                tint = if (active) IconGreen else DisabledIcon,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Texto y Badge
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = contract.type.label,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = if (active) Black else Color.Gray
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Badge de estado
                StatusBadge(
                    status = contract.status,
                    habilited = active
                )
            }

            // Flecha de navegación
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
        // Línea divisoria
        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
    }
}