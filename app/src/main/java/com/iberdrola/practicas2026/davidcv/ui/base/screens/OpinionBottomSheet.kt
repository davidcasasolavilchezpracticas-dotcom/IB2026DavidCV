package com.iberdrola.practicas2026.davidcv.ui.base.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.ui.base.composables.SatisfactionPicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpinionBottomSheet(
    onDismiss: () -> Unit,
    onLaterClick: () -> Unit,
    onRatingSelected: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() }, // La barrita horizontal superior
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título
            Text(
                text = "Tu opinión nos importa",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Pregunta
            Text(
                text = "¿Cómo de probable es que recomiendes esta app a amigos o familiares para que realicen sus gestiones?",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
            Spacer(modifier = Modifier.height(32.dp))

            // Selector de Emojis
            SatisfactionPicker(
                onRatingSelected = {
                    onRatingSelected()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Botón "Responder más tarde"
            TextButton(onClick = onLaterClick) {
                Text(
                    text = "Responder más tarde",
                    style = MaterialTheme.typography.labelLarge.copy(
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF006633) // Verde del tema anterior
                    )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}