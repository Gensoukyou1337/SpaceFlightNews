package com.ivan.spaceflightnews.screens.login

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ivan.spaceflightnews.Login
import com.ivan.spaceflightnews.Main
import com.ivan.spaceflightnews.common.LoginCore
import com.ivan.spaceflightnews.jobs.LogoutAfterTenMinutesWork
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import timber.log.Timber
import java.util.concurrent.TimeUnit


@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = koinViewModel(),
    loginCore: LoginCore = koinInject()
) {
    val context = LocalContext.current
    val hasIdToken = remember { mutableStateOf<Boolean?>(null) }
    val showPermissionRationaleDialog = remember { mutableStateOf(false) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Timber.i("notif permission granted go to main from launcher")
            navigateToMain(navController = navController)
        } else {
            showPermissionRationaleDialog.value = true
        }
    }

    LaunchedEffect(true) {
        loginCore.queryUserIDToken()
        // Changes to User ID Token in SharedPreferences is automatically reflected here.
        loginCore.currentIDTokenSharedFlow.collect {
            hasIdToken.value = it.isNotEmpty()
            if (it.isNotEmpty()) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    navigateToMain(navController = navController)
                }
            }
        }
    }

    if (hasIdToken.value == false) {
        Column {
            Text(text = "Please Login to access this app")
            Button(onClick = {
                loginCore.login(context) {
                    val oneTimeWorkRequest =
                        OneTimeWorkRequestBuilder<LogoutAfterTenMinutesWork>()
                            .setInitialDelay(10, TimeUnit.MINUTES)
                            .build()
                    WorkManager.getInstance(context).enqueue(oneTimeWorkRequest)
                }
            }) {
                Text(text = "Login")
            }
        }
    }
    
    if (showPermissionRationaleDialog.value) {
        AlertDialog(
            onDismissRequest = { 
                showPermissionRationaleDialog.value = false
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            },
            confirmButton = {
                showPermissionRationaleDialog.value = false
                navigateToMain(navController = navController)
            },
            text = { Text(stringResource(id = com.ivan.spaceflightnews.R.string.permission_deny_alert)) },
        )
    }
}

private fun navigateToMain(navController: NavController) {
    navController.navigate(Main) {
        popUpTo(Login) {inclusive = true}
    }
}