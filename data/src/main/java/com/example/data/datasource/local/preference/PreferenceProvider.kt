package com.example.data.datasource.local.preference

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferenceProvider(
    private val context: Context
) {
    private val Context.dataStore by preferencesDataStore("user_preferences")

    suspend fun updateTheme(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[APP_THEME] = theme
        }
    }

    fun observeTheme(): Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[APP_THEME] ?: "DEVICE"
        }
    }

    suspend fun updateEncryptedAccessToken(accessToken: String) {
        context.dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN] = accessToken
        }
    }

    fun observeEncryptedAccessToken(): Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[ACCESS_TOKEN] ?: ""
        }
    }


    suspend fun updateEncryptedRefreshToken(refreshToken: String) {
        context.dataStore.edit { preferences ->
            preferences[REFRESH_TOKEN] = refreshToken
        }
    }

    fun observeEncryptedRefreshToken(): Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[REFRESH_TOKEN] ?: ""
        }
    }

    companion object {
        val APP_THEME = stringPreferencesKey("app_theme")
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }
}