package com.petbulance.presentation.utils.hooks.login

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.Scope
import com.petbulance.domain.utils.LOGGER_TAG

private const val TAG = "$LOGGER_TAG - GoogleLoginManager"

private val GOOGLE_SCOPES = listOf(
    Scope("https://www.googleapis.com/auth/userinfo.email"),
    Scope("https://www.googleapis.com/auth/userinfo.profile"),
)

/**
 * Returns a function that when called, initiates the Google login flow.
 * On success, it passes the OAuth2 access token to the [onResult] callback.
 * On failure, it passes null to the [onResult] callback.
 *
 * The access token is used by the server to call Google's userinfo endpoint
 * (https://www.googleapis.com/oauth2/v2/userinfo) as a Bearer token.
 */
@Composable
fun rememberGoogleLoginManager(
    onResult: (String?) -> Unit
): () -> Unit {
    val context = LocalContext.current
    val activity = context as Activity
    val authorizationClient = remember(activity) { Identity.getAuthorizationClient(activity) }

    val consentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_CANCELED) {
            Log.d(TAG, "User cancelled the consent flow.")
            onResult(null)
            return@rememberLauncherForActivityResult
        }

        try {
            val authorizationResult =
                authorizationClient.getAuthorizationResultFromIntent(result.data)
            val accessToken = authorizationResult.accessToken
            if (accessToken != null) {
                onResult(accessToken)
            } else {
                Log.e(TAG, "Access token is null after consent.")
                onResult(null)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to parse authorization result from consent intent.", e)
            onResult(null)
        }
    }

    return remember(authorizationClient, consentLauncher) {
        {
            val authorizationRequest = AuthorizationRequest.builder()
                .setRequestedScopes(GOOGLE_SCOPES)
                .build()

            authorizationClient.authorize(authorizationRequest)
                .addOnSuccessListener { authorizationResult ->
                    val pendingIntent = authorizationResult.pendingIntent
                    if (authorizationResult.hasResolution() && pendingIntent != null) {
                        try {
                            consentLauncher.launch(
                                IntentSenderRequest.Builder(pendingIntent.intentSender).build()
                            )
                        } catch (e: Exception) {
                            Log.e(TAG, "Failed to launch consent UI.", e)
                            onResult(null)
                        }
                    } else {
                        val accessToken = authorizationResult.accessToken
                        if (accessToken != null) {
                            onResult(accessToken)
                        } else {
                            Log.e(TAG, "Access token is null without consent resolution.")
                            onResult(null)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Authorization request failed.", e)
                    onResult(null)
                }
        }
    }
}
