package com.iberdrola.practicas2026.davidcv.ui.base.composables.initial

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.theme.White

@Composable
fun WelcomeHeader() {
    Text(
        text = stringResource(R.string.isTitle),
        style = MaterialTheme.typography.titleLarge.copy(fontSize = 28.sp),
        modifier = Modifier
            .padding(horizontal = LocalSpacing.current.xl)
            .padding(top = LocalSpacing.current.mega),
        fontWeight = FontWeight.Bold,
        color = White
    )
}