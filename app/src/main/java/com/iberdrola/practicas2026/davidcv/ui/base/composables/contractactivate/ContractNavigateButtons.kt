package com.iberdrola.practicas2026.davidcv.ui.base.composables.contractactivate

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsState

@Composable
fun ContractNavigateButtons(
    enable: Boolean,
    onBack: () -> Unit,
    onNext: () -> Unit,
) {
    HorizontalDivider(
        thickness = 1.dp,
        color = Color.LightGray,
        modifier = Modifier
            .layout{ measurable, constraints ->
                val placeable = measurable.measure(
                    constraints.copy(
                        maxWidth = constraints.maxWidth + 64.dp.roundToPx()
                    )
                )
                layout(placeable.width, placeable.height) {
                    placeable.place(0, 0)
                }
            },
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = LocalSpacing.current.lg),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            border = BorderStroke(1.dp, Color(0xFF006633)),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(stringResource(R.string.cscBack), color = Color(0xFF006633))
        }

        Button(
            onClick = onNext,
            modifier = Modifier
                .weight(1f)
                .height(48.dp),
            enabled = enable,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE0E8E3),
                contentColor = Color(0xFF006633)
            ),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(stringResource(R.string.cscNext))
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    HorizontalDivider(
        thickness = 1.dp,
        color = Color.LightGray,
        modifier = Modifier
            .layout{ measurable, constraints ->
                val placeable = measurable.measure(
                    constraints.copy(
                        maxWidth = constraints.maxWidth + 64.dp.roundToPx()
                    )
                )
                layout(placeable.width, placeable.height) {
                    placeable.place(0, 0)
                }
            },
    )
}