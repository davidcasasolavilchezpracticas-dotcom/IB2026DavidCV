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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.model.account.Account
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.composables.contractactivate.LegalTextItem
import com.iberdrola.practicas2026.davidcv.ui.navigation.DataStoreViewModel
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsState

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
            Column {
                IconButton(onClick = onClose, modifier = Modifier.align(Alignment.End)) {
                    Icon(Icons.Default.Close, contentDescription = stringResource(R.string.close))
                }
                Text(
                    text = stringResource(R.string.cacActivateElectronicBill),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = LocalSpacing.current.lg)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { 0.5f },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp),
                    color = Color(0xFF006633),
                    trackColor = Color(0xFFE0E8E3)
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
            Text(
                text = stringResource(R.string.cacTitleAccountEmail),
                style = MaterialTheme.typography.bodySmall
            )
            Text(text = onCensurator(account?.email ?: "correoejemplo@gmail.com"), fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(LocalSpacing.current.xl))

            Text(
                text = stringResource(R.string.cacTitleEmailLinked),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            TextField(
                value = state.emailTry,
                onValueChange = onEmailChanged,
                label = { Text(stringResource(R.string.cacLabelEmailText)) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Gray,
                    unfocusedIndicatorColor = Color.LightGray
                ),
                isError = state.emailTry.isNotEmpty() && !state.isEmailValid
            )

            if (state.emailTry.isNotEmpty() && !state.isEmailValid) {
                Text(
                    text = stringResource(R.string.cacErrorInvalidEmail),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(LocalSpacing.current.xl))

            Text(
                text = stringResource(R.string.cscBasicInfo),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            LegalTextItem(
                R.string.cscLegalTextResponsable,
                R.string.cscLegalTextResponsableDescription
            )
            LegalTextItem(R.string.cscLegalTextFinalidad, R.string.cscLegalTextFinalidadDescription)
            LegalTextItem(R.string.cscLegalTextDerechos, R.string.cscLegalTextDerechosDescription)

            Spacer(modifier = Modifier.height(LocalSpacing.current.lg))

            Row(verticalAlignment = Alignment.Top) {
                Checkbox(
                    checked = state.isAcceptedPolicy,
                    onCheckedChange = onAcceptedChanged,
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF006633))
                )
                Text(
                    text = stringResource(R.string.cscPolicy),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = LocalSpacing.current.la)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = LocalSpacing.current.lg),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    border = BorderStroke(1.dp, Color(0xFF006633)),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(stringResource(R.string.cscBack), color = Color(0xFF006633))
                }

                Button(
                    onClick = onNext,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    enabled = state.canSubmitEmailAndPolicy,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE0E8E3),
                        contentColor = Color(0xFF006633)
                    ),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Text(stringResource(R.string.cscNext))
                }
            }
        }
    }
}
