package com.example.presentation.utils.hooks.login

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import com.example.domain.utils.LOGGER_TAG
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

/**
 * Returns a function that when called, initiates the Kakao login flow.
 * On success, it passes the access token to the [onResult] callback.
 * On failure, it passes null to the [onResult] callback.
 *
 * @param onResult Callback to be invoked when the login flow is complete.
 * The parameter is the access token on success, or null on failure.
 * @return A function that can be called to initiate the Kakao login flow.
 */
@Composable
fun rememberKakaoLoginManager(
    onResult: (String?) -> Unit
): () -> Unit {

    if (LocalInspectionMode.current) {
        return remember { {} }
    }

    val context = LocalContext.current

    val keyHash = com.kakao.sdk.common.util.Utility.getKeyHash(context)
    Log.d("KeyHash", "Current Kakao Key Hash is: $keyHash")

    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                Log.d("$LOGGER_TAG - KakaoLogin", "User cancelled the login flow.")
            } else {
                Log.d("$LOGGER_TAG - KakaoLogin", "Kakao login failed.", error)
            }
            onResult(null)
        } else if (token != null) {
            Log.d("KakaoLoginManager", "Kakao login success. Token: ${token.accessToken}")
            onResult(token.accessToken)
        }
    }

    return {
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context, callback = callback)
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }
}