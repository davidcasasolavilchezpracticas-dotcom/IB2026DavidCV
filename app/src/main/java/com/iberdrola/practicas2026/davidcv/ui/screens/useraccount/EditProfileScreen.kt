package com.iberdrola.practicas2026.davidcv.ui.screens.useraccount

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.composables.user_account.FlexibleAvatar
import com.iberdrola.practicas2026.davidcv.ui.navigation.DataStoreViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    viewModel: DataStoreViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val account by viewModel.account.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Estados locales para la edición
    var name by remember { mutableStateOf(account?.name ?: "") }
    var email by remember { mutableStateOf(account?.email ?: "") }
    var profileImage by remember { mutableStateOf(account?.profileImage) }

    // Sincronizar estados locales cuando el account se cargue por primera vez o cambie externamente
    LaunchedEffect(account) {
        account?.let {
            if (name.isEmpty()) name = it.name
            if (email.isEmpty()) email = it.email
            if (profileImage == null) profileImage = it.profileImage
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { tempUri ->
            scope.launch {
                val localUri = viewModel.saveImageToInternalStorage(context, tempUri)
                if (localUri != null) {
                    profileImage = localUri
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.epsTitle), fontWeight = FontWeight.Bold) },
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(LocalSpacing.current.xl),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Foto con icono de cámara para indicar que es editable
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
                        onImageSelected = {
                            launcher.launch("image/*")
                        }
                    )
                }
                // Botón flotante de cámara
                Surface(
                    shape = CircleShape,
                    color = Color(0xFF006633),
                    modifier = Modifier
                        .size(36.dp)
                        .offset(x = (-4).dp, y = (-4).dp)
                ) {
                    Icon(Icons.Default.PhotoCamera, null, Modifier.padding(LocalSpacing.current.sm), Color.White)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Campos de entrada
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.epsName)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text(stringResource(R.string.epsEmail)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.weight(1f))

            // Botón Guardar
            Button(
                onClick = {
                    account?.let {
                        viewModel.saveAccount(it.copy(name = name, email = email, profileImage = profileImage))
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF006633)),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Icon(Icons.Default.Check, null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.epsSaveChanges))
            }
        }
    }
}
