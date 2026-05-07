package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractactivate

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.model.account.Account
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.composables.contractactivate.CheckBoxPolite
import com.iberdrola.practicas2026.davidcv.ui.base.composables.contractactivate.ContractNavigateButtons
import com.iberdrola.practicas2026.davidcv.ui.base.composables.contractactivate.ContractTopAppBar
import com.iberdrola.practicas2026.davidcv.ui.base.composables.contractactivate.LegalTextItem
import com.iberdrola.practicas2026.davidcv.ui.base.composables.contractactivate.PoliteText
import com.iberdrola.practicas2026.davidcv.ui.base.composables.contractactivate.ProgressBar
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsState
import com.iberdrola.practicas2026.davidcv.ui.theme.EnergyGreen
import com.iberdrola.practicas2026.davidcv.ui.theme.IB2026DavidCVTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContractActivateContent(
    state: ContractActionsState,
    account: Account?,
    onCensurator: (String) -> String,
    onEmailChanged: (String) -> Unit,
    onAcceptedChanged: (Boolean) -> Unit,
    onBack: () -> Unit,
    onClose: () -> Unit,
    onNext: () -> Unit
) {
    Scaffold(
        topBar = {
            ContractTopAppBar(
                title = R.string.cacActivateElectronicBill,
                progress = 0.5f,
                onClose = onClose
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                ContractNavigateButtons(
                    enable = state.canSubmitEmailAndPolicy,
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
                .padding(LocalSpacing.current.lg)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(LocalSpacing.current.md))

            Text(
                text = stringResource(R.string.cacTitleAccountEmail),
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = onCensurator(account?.email ?: "correoejemplo@gmail.com"),
                style = MaterialTheme.typography.titleSmall
            )

            Spacer(modifier = Modifier.height(LocalSpacing.current.xxl))

            Text(
                text = stringResource(R.string.cacTitleEmailLinked),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(LocalSpacing.current.sm))

            TextField(
                value = state.emailTry,
                onValueChange = onEmailChanged,
                label = { Text(stringResource(R.string.cacLabelEmailText)) },
                modifier = Modifier.fillMaxWidth(),
                isError = state.emailTry.isNotEmpty() && !state.isEmailValid,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Gray,
                    unfocusedIndicatorColor = Color.LightGray
                ),
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

            Spacer(modifier = Modifier.height(LocalSpacing.current.xxl))

            Text(
                text = stringResource(R.string.cscBasicInfo),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            LegalTextItem(R.string.cscLegalTextResponsable,R.string.cscLegalTextResponsableDescription)
            Spacer(modifier = Modifier.height(LocalSpacing.current.sm))

            LegalTextItem(R.string.cscLegalTextFinalidad, R.string.cscLegalTextFinalidadDescription)
            Spacer(modifier = Modifier.height(LocalSpacing.current.sm))

            LegalTextItem(R.string.cscLegalTextDerechos, R.string.cscLegalTextDerechosDescription)
            Spacer(modifier = Modifier.height(LocalSpacing.current.lg))

            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .padding(LocalSpacing.current.sm)
            ) {
                CheckBoxPolite(
                    value = state.isAcceptedPolicy,
                    onCheckedChange = onAcceptedChanged,
                )

                Spacer(modifier = Modifier.width(LocalSpacing.current.sm))

                PoliteText(
                    txt1 = R.string.csc_Txt1Policy,
                    spTxt1 = R.string.csc_spTxt1Policy,
                    txt2 = R.string.csc_Txt2Policy
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ContractActivateContentPreview() {
    IB2026DavidCVTheme {
        ContractActivateContent(
            state = ContractActionsState(
                emailTry = "ejemplo@correo.com",
                isAcceptedPolicy = true
            ),
            account = Account(
                id = 1,
                name = "David",
                email = "david@example.com"
            ),
            onCensurator = { it },
            onEmailChanged = {},
            onAcceptedChanged = {},
            onBack = {},
            onClose = {},
            onNext = {}
        )
    }
}
