package com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractactionsuccess

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsState
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.permissions.AppPermissions
import com.iberdrola.practicas2026.davidcv.ui.base.common.ClickEventManager
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalClickManager
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.common.SafeClickTools.Companion.canExecuteMethod
import com.iberdrola.practicas2026.davidcv.ui.helper.NotificationHandler
import com.iberdrola.practicas2026.davidcv.ui.helper.rememberPermissionsLauncher
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActions
import com.iberdrola.practicas2026.davidcv.ui.theme.EnergyGreen
import com.iberdrola.practicas2026.davidcv.ui.theme.White

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun ContractActionSuccessContent(
    state: ContractActionsState,
    censurator: (String) -> String,
    manager: ClickEventManager,
    onAccept: () -> Unit,
    onClose: () -> Unit
) {
    var enableEnd by remember { mutableStateOf(true) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(EnergyGreen)
            .padding(LocalSpacing.current.xl)
    ) {
        IconButton(
            onClick = {
                canExecuteMethod(
                    manager,
                ) { onClose() }
            },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(R.string.close),
                tint = Color.White
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.ThumbUp,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = stringResource(
                    when (state.action) {
                        ContractActions.MODIFYEMAIL -> R.string.cascTitleModify
                        ContractActions.MODIFYSTATUSEMAIL -> R.string.cascTitleActivate
                        ContractActions.MODIFYPHONE -> R.string.cascTitleModifyPhone
                    }
                ),
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.emailForSuccessText) + censurator(state.emailTry),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )
        }

        Button(
            onClick = {
                canExecuteMethod(
                    manager,
                ) {
                    enableEnd = false
                    onAccept()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .align(Alignment.BottomCenter),
            colors = ButtonDefaults.buttonColors(
                containerColor = White,
                contentColor = EnergyGreen
            ),
            shape = RoundedCornerShape(28.dp),
            enabled = enableEnd
        ) {
            Text(
                text = stringResource(R.string.cascAccept),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}