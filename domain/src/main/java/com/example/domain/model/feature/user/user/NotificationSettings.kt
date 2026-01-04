package com.example.domain.model.feature.user.user

data class NotificationSettings(
    val isAllEnabled: Boolean,
    val isEventEnabled: Boolean,
    val isMarketingEnabled: Boolean
)