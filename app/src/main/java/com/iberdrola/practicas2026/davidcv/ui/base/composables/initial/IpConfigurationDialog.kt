package com.iberdrola.practicas2026.davidcv.ui.base.composables.initial

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.theme.White

@Composable
fun IpConfigurationDialog(
    initialIp: String,    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var tempIp by remember { mutableStateOf(initialIp) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = White,
        title = { Text(text = stringResource(R.string.ad_ConfigureIPTitle)) },
        text = {
            Column {
                Text(text = stringResource(R.string.ad_ConfigureIPText))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = tempIp,
                    onValueChange = { tempIp = it },
                    label = { Text(text = stringResource(R.string.ad_ConfigureIPLabel)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    )
                )
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(tempIp) }) {
                Text(text = stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}