package com.iberdrola.practicas2026.davidcv.ui.base.composables.contractlist


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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.domain.model.contract.Contract
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content.StatusBadge
import com.iberdrola.practicas2026.davidcv.ui.theme.Black
import com.iberdrola.practicas2026.davidcv.ui.theme.IconGreen

@Composable
fun ContractItem(
    contract: Contract,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            )
            .padding(
                horizontal = LocalSpacing.current.xl,
                vertical = LocalSpacing.current.sm
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
            Icon(
                imageVector = contract.type.icon,
                contentDescription = null,
                tint = IconGreen,
                modifier = Modifier.size(36.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Texto y Badge
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = contract.type.label,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = Black
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Badge de estado
                StatusBadge(
                    status = contract.status
                )
            }

            // Flecha de navegación
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(40.dp)
            )
        }
    }
}