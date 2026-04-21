package com.iberdrola.practicas2026.davidcv.ui.base.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.iberdrola.practicas2026.davidcv.ui.theme.EnergyGreen

@Composable
fun AlertDialogOK(
    icon: ImageVector,
    titulo:String,
    text: String,
    confirmText: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = EnergyGreen,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = titulo,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = text,
                fontSize = 17.sp,
                lineHeight = 22.sp
            )
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        ),
        containerColor = Color.White,
        tonalElevation = 8.dp,
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = EnergyGreen,
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .padding(6.dp)
            ) {
                Text(
                    text = confirmText,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    )
}

@Preview (
    name = "AlertDialog Ok",
    fontScale = 1.2f,
    showBackground = true
)
@Composable
fun AlertDialogOKPreview() {
    AlertDialogOK(
        icon = Icons.Default.Check,
        titulo = "Añadir",
        text = "Mensaje del cuadro dialogo",
        confirmText = "Aceptar",
        onDismiss = {}
    )
}
