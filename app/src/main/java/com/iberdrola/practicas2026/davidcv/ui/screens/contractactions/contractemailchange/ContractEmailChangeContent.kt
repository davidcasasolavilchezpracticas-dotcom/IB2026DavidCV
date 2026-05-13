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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.base.common.ClickEventManager
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalClickManager
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.common.SafeClickTools.Companion.canExecuteMethod
import com.iberdrola.practicas2026.davidcv.ui.base.composables.contractactivate.ContractNavigateButtons
import com.iberdrola.practicas2026.davidcv.ui.base.composables.contractactivate.ContractTopAppBar
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsState
import com.iberdrola.practicas2026.davidcv.ui.theme.EnergyGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContractEmailChangeContent(
    onEmailChanged: (String) -> Unit,
    state: ContractActionsState,
    manager: ClickEventManager,
    onClose: () -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    Scaffold(
        topBar = {
            ContractTopAppBar(
                title = R.string.ceccTitle,
                progress = 0.5f,
                onClose = {
                    canExecuteMethod(
                        manager,
                    ) { onClose() }
                },
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                ContractNavigateButtons(
                    enable = state.canSubmitEmail,
                    onBack = {
                        canExecuteMethod(
                            manager,
                        ) { onBack() }
                    },
                    onNext = {
                        canExecuteMethod(
                            manager,
                        ) { onNext() }
                    },
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(LocalSpacing.current.la)
        ) {
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
                label = {
                    Text(
                        text = stringResource(R.string.ceccLabelNewEmail),
                        color = Color.Gray
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.DarkGray,
                    unfocusedIndicatorColor = Color.LightGray,
                    cursorColor = EnergyGreen,
                    selectionColors = TextSelectionColors(
                        handleColor = EnergyGreen,
                        backgroundColor = EnergyGreen.copy(alpha = 0.4f)
                    )
                ),
                singleLine = true,
                isError = state.emailTry.isNotEmpty() && !state.isEmailValid,
                keyboardOptions = KeyboardOptions.Default.copy(
                    capitalization = KeyboardCapitalization.None,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                )
            )

            if (state.emailTry.isNotEmpty() && !state.isEmailValid) {
                Text(
                    text = stringResource(R.string.OnErrorEmail),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}