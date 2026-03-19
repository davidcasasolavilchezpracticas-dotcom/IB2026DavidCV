package com.iberdrola.practicas2026.davidcv.ui.base.composables.user_account

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.iberdrola.practicas2026.davidcv.domain.model.account.Account

@Composable
fun FlexibleAvatar(
    profileImage: Any?,
    onImageSelected: () -> Unit
) {

    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(Color(0xFFE0E8E3))
            .clickable { onImageSelected() },
        contentAlignment = Alignment.Center
    ) {
        if (profileImage != null) {
            // AsyncImage detecta automáticamente si es Uri o ImageVector
            AsyncImage(
                model = profileImage,
                contentDescription = "Imagen de perfil",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size(60.dp),
                tint = Color(0xFF006633)
            )
        }
    }
}