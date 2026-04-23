package com.iberdrola.practicas2026.davidcv.ui.base.composables.initial

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.model.account.Account

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InitialTopBar(account: Account?, onProfileClick: () -> Unit) {
    // Lógica para determinar qué imagen mostrar, manejando nulos y tipos incorrectos (Double/String ID)
    val rawImage = account?.profileImage ?: R.drawable.profile_picture
    val model = when (rawImage) {
        is Double -> rawImage.toInt()
        is String -> rawImage.toIntOrNull() ?: rawImage
        else -> rawImage
    }

    TopAppBar(
        title = { Text("") },
        actions = {
            IconButton(
                onClick = onProfileClick,
                modifier = Modifier.size(48.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp) // Tamaño estándar para avatar en TopAppBar
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = model,
                        contentDescription = "Perfil",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        error = null,
                        fallback = null
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF006633),
            actionIconContentColor = Color.White
        )
    )
}
