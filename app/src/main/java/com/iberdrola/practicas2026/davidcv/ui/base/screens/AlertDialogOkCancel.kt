package com.iberdrola.practicas2026.davidcv.ui.base.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties

@Composable
fun AlertDialogOkCancel(
    title: String = "Confirmación",
    text: String,
    okText: String = "Aceptar",
    cancelText: String = "Cancelar",
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = Color(0xFF1565C0),
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Text(
                text = text,
                fontSize = 17.sp,
                lineHeight = 22.sp
            )
        },
        containerColor = Color(0xFFF4F6F8), // Gris claro
        tonalElevation = 8.dp,

        // BOTONES OK / CANCELAR
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1565C0),
                    contentColor = Color.White
                ),
                modifier = Modifier.padding(end = 6.dp, bottom = 6.dp)
            ) {
                Text(
                    text = okText,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                border = BorderStroke(1.dp, Color(0xFF1565C0)),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF1565C0)
                ),
                modifier = Modifier.padding(start = 6.dp, bottom = 6.dp)
            ) {
                Text(
                    text = cancelText,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

@Preview (
    name = "AlertDialog Ok Cancel",
    fontScale = 1.2f,
    showBackground = true
)
@Composable
fun PreviewAlertDialogOkCancel() {
    MaterialTheme {
        AlertDialogOkCancel(
            title = "¿Confirmar acción?",
            text = "Esta acción no se puede deshacer.\n¿Deseas continuar?",
            okText = "Aceptar",
            cancelText = "Cancelar",
            onConfirm = {},
            onDismiss = {}
        )
    }
}













