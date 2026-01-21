package com.petbulance.presentation.utils.hooks.login

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.petbulance.domain.utils.LOGGER_TAG
import com.navercorp.nid.NidOAuth

@Composable
fun rememberNaverLoginManager(
    onResult: (String?) -> Unit
): () -> Unit {
    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult(),
    ) { result ->

        if (result.resultCode == Activity.RESULT_CANCELED) {
            Log.d("$LOGGER_TAG - NaverLoginManager", "User cancelled the login flow.")
            onResult(null)
            return@rememberLauncherForActivityResult
        }

        if (result.resultCode != Activity.RESULT_OK) {
            Log.w("$LOGGER_TAG - NaverLoginManager", "Login failed with resultCode: ${result.resultCode}")
            onResult(null)
            return@rememberLauncherForActivityResult
        }

        val accessToken = NidOAuth.getAccessToken()
        if (accessToken != null) {
            onResult(accessToken)
        } else {
            Log.e("$LOGGER_TAG - NaverLoginManager", "AccessToken is null despite RESULT_OK")
            onResult(null)
        }
    }

    return remember(context, launcher) {
        {
            NidOAuth.requestLogin(context, launcher)
        }
    }
}