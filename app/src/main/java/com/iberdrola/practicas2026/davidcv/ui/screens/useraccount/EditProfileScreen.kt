package com.iberdrola.practicas2026.davidcv.ui.screens.useraccount

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.logEvent
import com.iberdrola.practicas2026.davidcv.R
import com.iberdrola.practicas2026.davidcv.domain.model.account.Account
import com.iberdrola.practicas2026.davidcv.ui.base.common.LocalSpacing
import com.iberdrola.practicas2026.davidcv.ui.base.composables.user_account.EditFields
import com.iberdrola.practicas2026.davidcv.ui.base.composables.user_account.ProfileImageHeader
import com.iberdrola.practicas2026.davidcv.ui.base.composables.user_account.SaveButton
import com.iberdrola.practicas2026.davidcv.ui.navigation.DataStoreViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    viewModel: DataStoreViewModel = hiltViewModel(),
    navController: NavHostController,
    analytics: FirebaseAnalytics
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        analytics.logEvent("EditProfileScreen") {
            param("eventType", "View")
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { tempUri ->
            scope.launch {
                val localUri = viewModel.saveImageToInternalStorage(context, tempUri)
                viewModel.onImageChange(localUri)
                analytics.logEvent("ChangeImage") {
                    param("eventType", "RelevantMovements")
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
            ProfileImageHeader(
                profileImage = state.profileImage,
                onImageClick = {
                    launcher.launch("image/*")
                    analytics.logEvent("ButtonImageChange") {
                        param("eventType", "Click")
                    }
                }
            )

            Spacer(modifier = Modifier.height(40.dp))

            EditFields(
                name = state.name,
                onNameChange = viewModel::onNameChange,
                email = state.email,
                onEmailChange = viewModel::onEmailChange,
                isEmailValid = state.isEmailValid
            )

            Spacer(modifier = Modifier.weight(1f))

            SaveButton(
                enabled = state.isEmailValid,
                onClick = {
                    state.account?.let {
                        viewModel.saveAccount(
                            Account(
                                id = it.id,
                                name = state.name,
                                email = state.email,
                                profileImage = state.profileImage
                            )
                        )
                        navController.popBackStack()
                    }
                    analytics.logEvent("ButtonSaveChange") {
                        param("eventType", "Click")
                    }
                }
            )
        }
    }
}
