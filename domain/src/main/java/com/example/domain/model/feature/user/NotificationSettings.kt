package com.example.domain.model.feature.user

data class NotificationSettings(
    val isAllEnabled: Boolean,
    val isEventEnabled: Boolean,
    val isMarketingEnabled: Boolean
)