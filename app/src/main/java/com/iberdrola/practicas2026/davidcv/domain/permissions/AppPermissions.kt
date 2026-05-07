package com.iberdrola.practicas2026.davidcv.domain.permissions

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

object AppPermissions {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    val Notifications = AppRuntimePermission(
        permission = Manifest.permission.POST_NOTIFICATIONS,
        minSdk = Build.VERSION_CODES.TIRAMISU // 33
    )

}