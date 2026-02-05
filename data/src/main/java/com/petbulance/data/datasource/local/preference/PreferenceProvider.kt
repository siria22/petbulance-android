package com.petbulance.data.datasource.local.preference

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
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

    suspend fun updateLastLoginPlatform(platform: String) {
        context.dataStore.edit { preferences ->
            preferences[LAST_LOGIN_PLATFORM] = platform
        }
    }

    fun observeLastLoginPlatform(): Flow<String> {
        return context.dataStore.data.map { prefs ->
            prefs[LAST_LOGIN_PLATFORM] ?: ""
        }
    }

    suspend fun updateAutoLoginEnabled(isEnabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_AUTO_LOGIN_ENABLED] = isEnabled
        }
    }

    fun observeAutoLoginEnabled(): Flow<Boolean> {
        return context.dataStore.data.map { prefs ->
            prefs[IS_AUTO_LOGIN_ENABLED] ?: true // Default ON
        }
    }

    companion object {
        val APP_THEME = stringPreferencesKey("app_theme")
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
        val LAST_LOGIN_PLATFORM = stringPreferencesKey("last_login_platform")
        val IS_AUTO_LOGIN_ENABLED = booleanPreferencesKey("is_auto_login_enabled") // Add this
    }
}