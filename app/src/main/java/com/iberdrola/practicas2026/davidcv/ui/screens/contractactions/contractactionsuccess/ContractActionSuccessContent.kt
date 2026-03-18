package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractactionsuccess

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsState
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActions

@Composable
fun ContractActionSuccessContent(
    state: ContractActionsState,
    censurator: (String) -> String,
    onAccept: () -> Unit,
    onClose: () -> Unit
) {
    // Usamos el color verde corporativo para toda la pantalla
    val corporateGreen = Color(0xFF006633)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(corporateGreen)
            .padding(LocalSpacing.current.xl)
    ) {
        // Icono de cerrar en la esquina superior derecha
        IconButton(
            onClick = onClose,
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.close),
                tint = Color.White
            )
        }

        // Contenido Central
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icono del pulgar hacia arriba (ThumbUp)
            Icon(
                imageVector = Icons.Outlined.ThumbUp,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Título de éxito
            Text(
                text = stringResource(
                    when(state.action) {
                        ContractActions.MODIFYEMAIL -> R.string.cascTitleModify
                        ContractActions.MODIFYSTATUS -> R.string.cascTitleDesactivate
                        ContractActions.MODIFYSTATUSEMAIL -> R.string.cascTitleActivate
                    }
                ),
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mensaje descriptivo
            Text(
                text = "Pronto recibirás un correo electrónico de verificación para recibir tus facturas en la dirección ${censurator(state.emailTry)}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
        }

        // Botón Aceptar en la parte inferior
        Button(
            onClick = onAccept,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = corporateGreen
            ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                text = stringResource(R.string.cascAccept),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}