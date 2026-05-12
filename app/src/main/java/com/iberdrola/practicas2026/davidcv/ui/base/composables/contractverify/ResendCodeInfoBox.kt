package com.iberdrola.practicas2026.davidcv.ui.base.composables.contractverify

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing

@Composable
fun ResendCodeInfoBox(
    onResendClick: () -> Unit,
    resendCode: Boolean,
    trys: Int
) {
    Surface(
        color = Color(0xFFE1F5FE),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(LocalSpacing.current.lg)
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = null,
                tint = Color.DarkGray,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = stringResource(R.string.rcibTitleResendCode),
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = stringResource(R.string.rcibTextFindCode),
                    style = MaterialTheme.typography.bodySmall
                )

                if (resendCode) {
                    when (trys) {
                        0 -> {
                            Text(
                                text = stringResource(R.string.rcibTextNoMoreResendCode),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        1 -> {
                            Text(
                                text = stringResource(R.string.rcibTextOneMoreResendCode),
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        else -> {
                            Text(
                                text = "Recuerda que hoy te quedan $trys intentos.",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }

                Text(
                    text = stringResource(R.string.rcibTextResendCode),
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .padding(top = LocalSpacing.current.xs)
                        .clickable(
                            onClick = onResendClick
                        ),
                    color = Color(0xFF003366)
                )
            }
        }
    }
}
