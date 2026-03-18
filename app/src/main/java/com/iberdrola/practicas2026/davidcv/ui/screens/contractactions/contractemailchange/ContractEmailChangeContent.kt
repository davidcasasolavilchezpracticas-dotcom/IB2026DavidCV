package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractemailchange

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContractEmailChangeContent(
    state: ContractActionsState,
    onEmailChanged: (String) -> Unit,
    onClose: () -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    Scaffold(
        topBar = {
            Column {
                // Icono de cierre a la derecha
                Box(modifier = Modifier.fillMaxWidth()) {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = stringResource(R.string.ceccClose)
                        )
                    }
                }

                Text(
                    text = stringResource(R.string.ceccTitle),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = LocalSpacing.current.lg)
                )

                Spacer(modifier = Modifier.height(LocalSpacing.current.md))

                // Barra de progreso (50% verde / 50% gris)
                Row(modifier = Modifier.fillMaxWidth().height(4.dp)) {
                    Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color(0xFF006633)))
                    Box(modifier = Modifier.weight(1f).fillMaxHeight().background(Color(0xFFE0E8E3)))
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(LocalSpacing.current.la)
        ) {
            Spacer(modifier = Modifier.height(LocalSpacing.current.lg))

            Text(
                text = stringResource(R.string.cacTitleEmailLinked),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(LocalSpacing.current.xxl))

            // Campo de texto estilo Material (solo línea inferior)
            TextField(
                value = state.emailTry,
                onValueChange = onEmailChanged,
                label = { Text(text = stringResource(R.string.ceccLabelNewEmail), color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.DarkGray,
                    unfocusedIndicatorColor = Color.LightGray,
                    cursorColor = Color.Black
                ),
                singleLine = true,
                isError = state.emailTry.isNotEmpty() && !state.isEmailValid
            )

            if (state.emailTry.isNotEmpty() && !state.isEmailValid) {
                Text(
                    text = "Introduce un email válido",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Botones inferiores
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = LocalSpacing.current.lg),
                horizontalArrangement = Arrangement.spacedBy(LocalSpacing.current.lg)
            ) {
                // Botón Anterior (Outlined)
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp),
                    border = BorderStroke(1.5.dp, Color(0xFF2E4D3E)),
                    shape = RoundedCornerShape(27.dp)
                ) {
                    Text(
                        text = stringResource(R.string.cscBack),
                        color = Color(0xFF2E4D3E),
                        fontWeight = FontWeight.Bold
                    )
                }

                // Botón Siguiente (Contained con estado deshabilitado)
                Button(
                    onClick = onNext,
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp),
                    enabled = state.canSubmitEmail,
                    shape = RoundedCornerShape(27.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE9F0EC), // Fondo suave
                        contentColor = Color(0xFF2E4D3E),   // Texto oscuro
                        disabledContainerColor = Color(0xFFF1F5F2),
                        disabledContentColor = Color.LightGray
                    )
                ) {
                    Text(
                        text = stringResource(R.string.cscNext),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
