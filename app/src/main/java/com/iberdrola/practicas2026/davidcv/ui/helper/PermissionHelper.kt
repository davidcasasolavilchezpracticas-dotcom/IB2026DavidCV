package com.iberdrola.practicas2026.davidcv.ui.helper

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.iberdrola.practicas2026.davidcv.domain.permissions.AppRuntimePermission

private const val TAG = "PermissionHelper"

/**
 * Hook para gestionar permisos de forma modular en Compose.
 * Utiliza rememberUpdatedState para que los callbacks siempre usen los datos más recientes.
 */
@Composable
fun rememberPermissionsLauncher(
    permissions: List<AppRuntimePermission>,
    onAllGranted: () -> Unit,
    onDenied: (denied: List<String>) -> Unit = {},
    onResultMap: (Map<String, Boolean>) -> Unit = {}
): () -> Unit {
    val context = LocalContext.current

    // Envolvemos los callbacks en updatedState para evitar clausuras obsoletas
    val currentOnAllGranted by rememberUpdatedState(onAllGranted)
    val currentOnDenied by rememberUpdatedState(onDenied)
    val currentOnResultMap by rememberUpdatedState(onResultMap)

    val permissionsToRequest = remember(permissions) {
        permissions.filter { it.appliesToDevice() }.map { it.permission }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        currentOnResultMap(result)
        val denied = result.filterValues { granted -> !granted }.keys.toList()
        if (denied.isEmpty()) currentOnAllGranted() else currentOnDenied(denied)
    }

    return remember(permissionsToRequest) {
        {
            executePermissionFlow(
                context = context,
                permissions = permissionsToRequest,
                onAlreadyGranted = { currentOnAllGranted() },
                launchSystemRequest = { launcher.launch(permissionsToRequest.toTypedArray()) }
            )
        }
    }
}

private fun executePermissionFlow(
    context: Context,
    permissions: List<String>,
    onAlreadyGranted: () -> Unit,
    launchSystemRequest: () -> Unit
) {
    if (permissions.isEmpty() || context.checkAllPermissionsGranted(permissions)) {
        onAlreadyGranted()
        return
    }

    val activity = context as? Activity
    if (activity?.shouldShowRationale(permissions) == true) {
        openSettings(context)
    } else {
        launchSystemRequest()
    }
}

fun Context.checkAllPermissionsGranted(permissions: List<String>): Boolean {
    return permissions.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }
}

fun Activity.shouldShowRationale(permissions: List<String>): Boolean {
    return permissions.any { ActivityCompat.shouldShowRequestPermissionRationale(this, it) }
}

fun openSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    context.startActivity(intent)
}
