package com.example.presentation.utils.hooks

import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.domain.utils.LOGGER_TAG
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

/**
 * 외부 권한을 요청하고, 권한이 승인되면 onPermissionGranted 함수를 실행하고, 거부되면
 * onPermissionDenied 함수를 실행. 권한 요청 중 에러가 발생하면 onPermissionError 함수를 실행 (없으면 실행 없이 종료).
 *
 * @param permission 요청하려는 권한 (Manifest.permission.~~)
 * @param onPermissionGranted 권한이 승인되면 실행되는 함수
 * @param onPermissionDenied 권한이 거부되면 실행되는 함수. 권한 요청을 다시 실행하기 위한 함수를 인자로 받습니다.
 * @param onPermissionError 권한 요청 중 에러가 발생하면 실행되는 함수. 에러 메시지를 인자로 받습니다.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionHandler(
    permission: String,
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    onPermissionError: ((String) -> Unit)? = null
) {
    val context = LocalContext.current

    val isPermissionDeclaredInManifest = remember(permission) {
        try {
            val packageInfo = context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.GET_PERMISSIONS
            )
            packageInfo.requestedPermissions?.contains(permission) ?: false
        } catch (e: Exception) {
            false
        }
    }

    if (!isPermissionDeclaredInManifest) {
        val errorMessage = "요청한 권한($permission)이 AndroidManifest.xml에 선언되지 않았음"
        Log.d(LOGGER_TAG, errorMessage)
        onPermissionError?.invoke(errorMessage)
        return
    }

    val permissionState = rememberPermissionState(permission = permission)

    LaunchedEffect(key1 = permissionState.status) {
        if (permissionState.status.isGranted) {
            onPermissionGranted()
        } else {
            onPermissionDenied()
        }
    }
}