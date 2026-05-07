package com.iberdrola.practicas2026.davidcv.ui.base.composables.contractverify

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.ContractActionsState
import com.iberdrola.practicas2026.davidcv.ui.screens.contractactions.contractverify.ContractVerifyEvents

@Composable
fun VerifyInstructionSection(
    state: ContractActionsState,
    events: ContractVerifyEvents
) {
    Column(
        modifier = Modifier
            .padding(LocalSpacing.current.la)
    ) {
        Text(
            stringResource(R.string.cvcSubtitleInsertCode),
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = stringResource(R.string.cvcTextVerifyIdentity) +
                    events.phoneCensurator(state.contract?.phone ?: "") +
                    stringResource(R.string.cvcTextVerifyIdentityEnd),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}