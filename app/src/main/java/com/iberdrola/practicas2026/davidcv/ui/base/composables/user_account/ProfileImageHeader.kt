package com.iberdrola.practicas2026.davidcv.ui.base.composables.user_account

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing

@Composable
fun ProfileImageHeader(
    profileImage: Any?,
    onImageClick: () -> Unit
) {
    Box(contentAlignment = Alignment.BottomEnd) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFFF1F5F2))
                .border(2.dp, Color(0xFF006633), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            FlexibleAvatar(
                profileImage = profileImage,
                onImageSelected = onImageClick
            )
        }
        Surface(
            shape = CircleShape,
            color = Color(0xFF006633),
            modifier = Modifier
                .size(36.dp)
                .offset(x = (-4).dp, y = (-4).dp)
        ) {
            Icon(
                imageVector = Icons.Default.PhotoCamera,
                contentDescription = null,
                modifier = Modifier.padding(LocalSpacing.current.sm),
                tint = Color.White
            )
        }
    }
}