package com.iberdrola.practicas2026.davidcv.ui.base.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.exception.BillException
import com.iberdrola.practicas2026.davidcv.domain.exception.ContractException
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.theme.White

@Composable
fun ErrorScreen(
    modifier: Modifier = Modifier,
    isConexionError: Boolean,
    onClick: () -> Unit,
    img: ImageVector,
    message: String,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(LocalSpacing.current.xl),
    ) {
        Icon(
            contentDescription = stringResource(R.string.error),
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(80.dp),
            imageVector = img,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (isConexionError) stringResource(R.string.serverFail) else stringResource(R.string.dataFail),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            modifier = Modifier.padding(horizontal = LocalSpacing.current.lg),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            text = message,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.padding(top = LocalSpacing.current.lg),
            onClick = { onClick() },
        ) {
            Text(
                text = if (isConexionError) stringResource(R.string.useLocal) else stringResource(R.string.goBack),
                modifier = Modifier.padding(horizontal = LocalSpacing.current.lg),
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = White,
            )
        }

    }
}