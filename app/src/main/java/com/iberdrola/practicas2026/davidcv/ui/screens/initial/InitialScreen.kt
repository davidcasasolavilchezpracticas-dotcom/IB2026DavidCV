package com.iberdrola.practicas2026.davidcv.ui.screens.initial

import android.widget.Button
import androidx.compose.foundation.clickable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.iberdrola.practicas2026.davidcv.ui.screens.billlist.BillListViewModel

@Composable
fun InitialScreen(
    navController: NavHostController,
    modifier: Modifier
) {
    Text(
        text = "Listado Facturas",
        modifier = modifier.clickable(
            onClick = { navController.navigate("list") }
        )
    )


}