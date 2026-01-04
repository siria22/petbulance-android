package com.example.presentation.utils.hooks.login

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import com.example.domain.utils.LOGGER_TAG
import com.example.presentation.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch

/**
 * Returns a function that when called, initiates the Google login flow.
 * On success, it passes the ID token to the [onResult] callback.
 * On failure, it passes null to the [onResult] callback.
 *
 * @param onResult Callback to be invoked when the login flow is complete. The parameter is the ID token on success, or null on failure.
 * @return A function that can be called to initiate the Google login flow.
 */
@Composable
fun rememberGoogleLoginManager(
    onResult: (String?) -> Unit
): () -> Unit {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val credentialManager = CredentialManager.create(context)

    return {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(BuildConfig.WEB_CLIENT_ID)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        coroutineScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context,
                )
                val credential = result.credential
                if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdToken = GoogleIdTokenCredential.createFrom(credential.data)
                    onResult(googleIdToken.idToken)
                } else {
                    onResult(null)
                }

            } catch (e: Exception) {
                when (e) {
                    is GetCredentialCancellationException -> {
                        Log.d("$LOGGER_TAG - GoogleLoginManager", "User cancelled the login flow.")
                        onResult(null)
                    }

                    is NoCredentialException -> {
                        Log.d("$LOGGER_TAG - GoogleLoginManager", "No Google accounts found on device.", e)
                        onResult(null)
                    }

                    else -> {
                        Log.e("$LOGGER_TAG - GoogleLoginManager", "An unexpected error occurred.", e)
                        onResult(null)
                    }
                }
            }
        }
    }
}