package com.iberdrola.practicas2026.davidcv.ui.base.composables.initial

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.navigation.Routes
import com.iberdrola.practicas2026.davidcv.ui.theme.EnergyGreen

@Composable
fun GeneralTopAppBar(
    currentRoute: String?,
    navController: NavController,
    handleBackNavigation: () -> Unit,
    isNavigating: Boolean = false
) {
    if (
        (
            currentRoute != Routes.INITIAL &&
            currentRoute != Routes.CONTRACT_ACTIVATE &&
            currentRoute != Routes.CONTRACT_EMAIL_CHANGE &&
            currentRoute != Routes.CONTRACT_PHONE_CHANGE &&
            currentRoute != Routes.CONTRACT_VERIFY &&
            currentRoute != Routes.CONTRACT_SUCCESS
            ) && currentRoute != null
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(LocalSpacing.current.xs)
                .padding(start = LocalSpacing.current.sm)
                .clip(RoundedCornerShape(12.dp))
                .clickable(enabled = !isNavigating) {
                    if (currentRoute == Routes.CONTRACT_INFO) {
                        navController.popBackStack()
                    }
                    handleBackNavigation()
                }
        ) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = null,
                tint = EnergyGreen,
                modifier = Modifier.size(36.dp)
            )
            Text(
                text = stringResource(R.string.matbTitle),
                color = EnergyGreen,
                modifier = Modifier
                    .padding(end = LocalSpacing.current.sm)
                    .drawBehind {
                        val strokeWidth = 1.dp.toPx()
                        val y = size.height + (-6).dp.toPx()
                        drawLine(
                            color = EnergyGreen,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = strokeWidth,
                        )
                    }
            )
        }
    }
}
