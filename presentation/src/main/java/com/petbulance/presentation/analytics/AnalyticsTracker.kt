package com.petbulance.presentation.analytics

interface AnalyticsTracker {
    fun trackScreen(screenName: String, screenClass: String? = null)
    fun trackEvent(eventName: String, params: Map<String, Any> = emptyMap())
    fun setUserId(userId: String?)
    fun setUserProperty(key: String, value: String?)
}
