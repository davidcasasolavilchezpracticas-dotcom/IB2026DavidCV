package com.iberdrola.practicas2026.davidcv.ui.base.composables.billlist_content

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.base.screens.AlertDialogOK
import com.iberdrola.practicas2026.davidcv.ui.screens.billlist.BillListViewModel.Companion.alertDialogText
import com.iberdrola.practicas2026.davidcv.ui.theme.White

@Composable
fun ShowAlertDialogs(
    getSelectedFilters: () -> List<String>,
    desactiveDeleteDialog: () -> Unit,
    desactiveAlertDialog: () -> Unit,
    onDeleteFilters: () -> Unit,
    alertDialogActive: Boolean,
    showDeleteDialog: Boolean,
    count: Int
) {
    if (alertDialogActive) {
        AlertDialogOK(
            icon = Icons.Default.HourglassEmpty,
            titulo = stringResource(R.string.blciTitle),
            text = stringResource(R.string.blciText),
            confirmText = stringResource(R.string.blciButtonOk),
            onDismiss = desactiveAlertDialog
        )
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = desactiveDeleteDialog,
            confirmButton = {
                TextButton(
                    onClick = {
                        onDeleteFilters()
                        desactiveDeleteDialog()
                    },
                ) {
                    Text(
                        text = stringResource(R.string.delete),
                        color = Color.Red
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = desactiveDeleteDialog) {
                    Text(
                        text = stringResource(R.string.cancel)
                    )
                }
            },
            title = { Text(text = stringResource(R.string.deleteFilters)) },
            text = {
                Column(){
                    Text(text = stringResource(R.string.deleteFiltersText))
                    Text(text = alertDialogText(getSelectedFilters, count))
                }
            },
            containerColor = White
        )
    }
}