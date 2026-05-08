package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractphonechange

import android.util.Log
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.composables.contractactivate.ContractNavigateButtons
import com.iberdrola.practicas2026.davidcv.ui.base.composables.contractactivate.ContractTopAppBar
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContractPhoneChangeContent(
    state: ContractActionsState,
    onPhoneChanged: (String) -> Unit,
    onClose: () -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    Log.d("Comprobaciones", "Contract Action -> ${state.action}")

    Scaffold(
        topBar = {
            ContractTopAppBar(
                title = R.string.cpccTitle,
                progress = 0.5f,
                onClose = onClose
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                ContractNavigateButtons(
                    enable = state.canSubmitPhone,
                    onBack = onBack,
                    onNext = onNext,
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
                text = stringResource(R.string.cacTitlePhoneLinked),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(LocalSpacing.current.xxl))

            // Campo de texto estilo Material (solo línea inferior)
            TextField(
                value = state.phoneTry,
                onValueChange = onPhoneChanged,
                label = { Text(text = stringResource(R.string.ceccLabelNewPhone), color = Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.DarkGray,
                    unfocusedIndicatorColor = Color.LightGray,
                    cursorColor = Color.Black
                ),
                singleLine = true,
                isError = state.phoneTry.isNotEmpty() && !state.isPhoneValid,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Phone,
                    imeAction = ImeAction.Done
                )
            )

            if (state.phoneTry.isNotEmpty() && !state.isPhoneValid) {
                Text(
                    text = stringResource(R.string.OnErrorPhone),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))}
    }
}
