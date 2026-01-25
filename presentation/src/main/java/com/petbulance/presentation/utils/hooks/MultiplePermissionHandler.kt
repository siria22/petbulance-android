package com.petbulance.presentation.utils.hooks

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HandleMultiplePermissions(
    permissions: List<String>,
    onAllGranted: () -> Unit = {},
    onDenied: (List<String>) -> Unit = {}
) {
    val permissionState: MultiplePermissionsState = rememberMultiplePermissionsState(
        permissions = permissions
    )

    LaunchedEffect(Unit) {
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        } else {
            onAllGranted()
        }
    }

    LaunchedEffect(permissionState.allPermissionsGranted) {
        if (permissionState.allPermissionsGranted) {
            onAllGranted()
        } else {
            // 거부된 권한이 있을 경우 처리
            val deniedPermissions = permissionState.revokedPermissions.map { it.permission }
            if (deniedPermissions.isNotEmpty()) {
                onDenied(deniedPermissions)
            }
        }
    }
}