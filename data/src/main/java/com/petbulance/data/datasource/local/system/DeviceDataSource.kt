package com.petbulance.data.datasource.local.system

import android.os.Build
import android.util.Log
import com.petbulance.domain.utils.LOGGER_TAG
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DeviceDataSource @Inject constructor() {

    suspend fun getFcmToken(): String {
        return try {
            FirebaseMessaging.getInstance().token.await()
        } catch (e: Exception) {
            Log.d(LOGGER_TAG, "Failed to get FCM Token: ${e.message}")
            ""
        }
    }

    fun getDeviceOs(): String {
        return "Android ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})"
    }
}