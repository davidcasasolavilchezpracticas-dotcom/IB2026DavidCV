package com.iberdrola.practicas2026.davidcv.ui.base.composables.contractverify

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing

@Composable
fun ResendCodeInfoBox(
    onResendClick: () -> Unit
) {
    Surface(color = Color(0xFFE1F5FE), shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.padding(LocalSpacing.current.lg)) {
            Icon(Icons.Outlined.Info, null, tint = Color.DarkGray, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(stringResource(R.string.rcibTitleResendCode), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleSmall)
                Text(stringResource(R.string.rcibTextFindCode), style = MaterialTheme.typography.bodySmall)
                Text(
                    stringResource(R.string.rcibTextResendCode),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .padding(top = LocalSpacing.current.xs)
                        .clickable { onResendClick() },
                    color = Color(0xFF003366)
                )
            }
        }
    }
}
