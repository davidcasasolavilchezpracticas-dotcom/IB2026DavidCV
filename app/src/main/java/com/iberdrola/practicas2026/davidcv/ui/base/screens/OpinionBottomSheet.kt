package com.iberdrola.practicas2026.davidcv.ui.base.screens

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.composables.satisfaction.SatisfactionPicker


/**
 * OpinionBottomSheet
 * Muestra un BottomSheet con una encuesta de satisfacción del usuario
 *
 * @param onDismiss Función a ejecutar al cerrar el BottomSheet
 * @param onLaterClick Función a ejecutar al hacer clic en el botón "Responder más tarde"
 * @param onRatingSelected Función a ejecutar al seleccionar una calificación
 */
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
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = LocalSpacing.current.xl, vertical = LocalSpacing.current.lg),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.opinion),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.bsText),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = LocalSpacing.current.sm)
            )

            Spacer(modifier = Modifier.height(24.dp))
            HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
            Spacer(modifier = Modifier.height(32.dp))

            SatisfactionPicker(
                onRatingSelected = {
                    onRatingSelected()
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(onClick = onLaterClick) {
                Text(
                    text = stringResource(R.string.respondLater),
                    style = MaterialTheme.typography.labelLarge.copy(
                        textDecoration = TextDecoration.Underline,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF006633)                     )
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}