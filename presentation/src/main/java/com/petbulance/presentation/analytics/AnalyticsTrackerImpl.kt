package com.petbulance.presentation.analytics

import android.content.Context
import android.os.Bundle
import com.facebook.appevents.AppEventsConstants
import com.facebook.appevents.AppEventsLogger
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject
import jakarta.inject.Singleton

@Singleton
class AnalyticsTrackerImpl @Inject constructor(
    @ApplicationContext context: Context
) : AnalyticsTracker {

    private val firebaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)
    private val metaLogger: AppEventsLogger = AppEventsLogger.newLogger(context)

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
        trackMetaEvent(eventName, params)
    }

    private fun trackMetaEvent(eventName: String, params: Map<String, Any>) {
        val metaEventName = META_EVENT_MAP[eventName] ?: return
        val metaParams = Bundle()

        when (eventName) {
            AnalyticsEvents.SEARCH_HOSPITAL_START -> {
                params[AnalyticsEvents.Params.PET_TYPE]?.toString()?.let {
                    metaParams.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, it)
                }
                params[AnalyticsEvents.Params.SEARCH_METHOD]?.toString()?.let {
                    metaParams.putString(AppEventsConstants.EVENT_PARAM_SEARCH_STRING, it)
                }
            }

            AnalyticsEvents.VIEW_HOSPITAL_DETAIL -> {
                params[AnalyticsEvents.Params.HOSPITAL_ID]?.toString()?.let {
                    metaParams.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, it)
                }
                metaParams.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, CONTENT_TYPE_HOSPITAL)
            }

            AnalyticsEvents.SUBMIT_REVIEW -> {
                metaParams.putInt(AppEventsConstants.EVENT_PARAM_MAX_RATING_VALUE, MAX_RATING)
                metaParams.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, CONTENT_TYPE_HOSPITAL)
                params[AnalyticsEvents.Params.HOSPITAL_ID]?.toString()?.let {
                    metaParams.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, it)
                }
                val rating = params[AnalyticsEvents.Params.RATING]?.toDoubleValue()
                if (rating != null) {
                    metaLogger.logEvent(metaEventName, rating, metaParams)
                    return
                }
            }

            AnalyticsEvents.CLICK_CALL_HOSPITAL -> {
                params[AnalyticsEvents.Params.HOSPITAL_ID]?.toString()?.let {
                    metaParams.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, it)
                }
            }
        }

        metaLogger.logEvent(metaEventName, metaParams)
    }

    private fun Any.toDoubleValue(): Double? = when (this) {
        is Double -> this
        is Int -> toDouble()
        is Long -> toDouble()
        is Float -> toDouble()
        else -> toString().toDoubleOrNull()
    }

    override fun setUserId(userId: String?) {
        firebaseAnalytics.setUserId(userId)
    }

    override fun setUserProperty(key: String, value: String?) {
        firebaseAnalytics.setUserProperty(key, value)
    }

    companion object {
        private const val MAX_PARAM_VALUE_LENGTH = 100
        private const val CONTENT_TYPE_HOSPITAL = "hospital"
        private const val META_EVENT_CONTACT = "Contact"
        private const val MAX_RATING = 5

        private val META_EVENT_MAP = mapOf(
            AnalyticsEvents.SEARCH_HOSPITAL_START to AppEventsConstants.EVENT_NAME_SEARCHED,
            AnalyticsEvents.VIEW_HOSPITAL_DETAIL to AppEventsConstants.EVENT_NAME_VIEWED_CONTENT,
            AnalyticsEvents.SUBMIT_REVIEW to AppEventsConstants.EVENT_NAME_RATED,
            AnalyticsEvents.CLICK_CALL_HOSPITAL to META_EVENT_CONTACT,
        )
    }
}
