package com.petbulance.presentation.analytics

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class AnalyticsTrackerImpl @Inject constructor(
    @ApplicationContext context: Context
) : AnalyticsTracker {

    private val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    override fun trackScreen(screenName: String, screenClass: String?) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
            screenClass?.let { putString(FirebaseAnalytics.Param.SCREEN_CLASS, it) }
        }
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    override fun trackEvent(eventName: String, params: Map<String, Any>) {
        val bundle = Bundle().apply {
            params.forEach { (key, value) ->
                when (value) {
                    is String -> putString(key, value.take(MAX_PARAM_VALUE_LENGTH))
                    is Long -> putLong(key, value)
                    is Int -> putInt(key, value)
                    is Double -> putDouble(key, value)
                    is Boolean -> putString(key, value.toString())
                    else -> putString(key, value.toString().take(MAX_PARAM_VALUE_LENGTH))
                }
            }
        }
        firebaseAnalytics.logEvent(eventName, bundle)
    }

    override fun setUserId(userId: String?) {
        firebaseAnalytics.setUserId(userId)
    }

    override fun setUserProperty(key: String, value: String?) {
        firebaseAnalytics.setUserProperty(key, value)
    }

    companion object {
        private const val MAX_PARAM_VALUE_LENGTH = 100
    }
}
