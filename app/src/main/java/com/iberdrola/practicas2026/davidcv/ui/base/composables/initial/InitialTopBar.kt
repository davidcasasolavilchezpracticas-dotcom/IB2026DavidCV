package com.iberdrola.practicas2026.davidcv.ui.base.composables.initial

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.iberdrola.practicas2026.davidcv.domain.model.account.Account

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InitialTopBar(account: Account?, onProfileClick: () -> Unit) {
    TopAppBar(
        title = { Text("") },
        actions = {
            IconButton(onClick = onProfileClick) {
                if (account?.profileImage == null) {
                    Icon(
                        Icons.Default.Person, null,
                        Modifier.size(60.dp), Color(0xFF006633)
                    )
                } else {
                    AsyncImage(
                        model = account.profileImage,
                        contentDescription = "Perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF006633))
    )
}