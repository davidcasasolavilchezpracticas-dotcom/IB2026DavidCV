package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractverify

import android.content.Context
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.composables.contractverify.ResendCodeInfoBox
import com.iberdrola.practicas2026.davidcv.ui.base.composables.contractverify.SuccessBanner
import com.iberdrola.practicas2026.davidcv.ui.navigation.DataStoreViewModel
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActions
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsState
import kotlinx.coroutines.delay
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContractVerifyContent(
    dataStoreViewModel: DataStoreViewModel,
    state: ContractActionsState,
    onVerifyCodeChanged: (String) -> Unit,
    generateNewCode: (Context) -> Unit,
    onLoadEnd: () -> Unit,
    onClose: () -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    var resendVerificationCode by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val trys = dataStoreViewModel.trys.collectAsState()


    Scaffold(
        topBar = {
            Column {
                IconButton(onClick = onClose, modifier = Modifier.align(Alignment.End)) {
                    Icon(
                        Icons.Default.Close,
                        stringResource(R.string.cvcClose),
                        tint = Color(0xFF006633)
                    )
                }
                Text(
                    text = stringResource(when (state.action) {
                        ContractActions.MODIFYEMAIL -> R.string.cvcTitleModifyEmail
                        ContractActions.MODIFYSTATUS -> R.string.cvcTitleDesactivate
                        ContractActions.MODIFYSTATUSEMAIL -> R.string.cvcTitleActivate
                        ContractActions.MODIFYPHONE -> R.string.cvcTitleModifyPhone
                    }) ,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(horizontal = LocalSpacing.current.lg)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(3f)
                            .fillMaxHeight()
                            .background(Color(0xFF006633))
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .background(Color(0xFFE0E8E3))
                    )
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
            Text(
                stringResource(R.string.cvcSubtitleInsertCode),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(32.dp))
            TextField(
                value = state.verifyCodeTry,
                onValueChange = { if (it.length <= 6) onVerifyCodeChanged(it) },
                label = { Text(stringResource(R.string.cvcTextFieldVerifyCode)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.DarkGray,
                    unfocusedIndicatorColor = Color.LightGray
                )
            )
            Spacer(modifier = Modifier.height(24.dp))
            ResendCodeInfoBox(
                trys = trys.value,
                onResendClick = {
                    resendVerificationCode = true
                    generateNewCode(context)
                    dataStoreViewModel.updateTrys(trys.value - 1)
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            if (resendVerificationCode) {
                onLoadEnd()
                SuccessBanner(
                    onDismiss = { resendVerificationCode = false },
                )
            }

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
                        .height(54.dp),
                    border = BorderStroke(1.5.dp, Color(0xFF2E4D3E)),
                    shape = RoundedCornerShape(27.dp)
                ) {
                    Text(stringResource(R.string.Back), color = Color(0xFF2E4D3E))
                }
                Button(
                    onClick = onNext,
                    enabled = state.canSubmitVerify,
                    modifier = Modifier
                        .weight(1f)
                        .height(54.dp),
                    shape = RoundedCornerShape(27.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE9F0EC),
                        contentColor = Color(0xFF2E4D3E)
                    )
                ) {
                    Text(stringResource(R.string.Next))
                }
            }
        }
    }
}
