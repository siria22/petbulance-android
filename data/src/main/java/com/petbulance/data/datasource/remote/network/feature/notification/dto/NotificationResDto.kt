package com.petbulance.data.datasource.remote.network.feature.notification.dto

import kotlinx.serialization.Serializable

@Serializable
data class PagingNotificationListResDto(
    val content: List<NotificationResDto>,
    val hasNext: Boolean,
    val lastNotificationId: Long? = null
)

@Serializable
data class NotificationResDto(
    val notificationId: Long,
    val type: String,
    val topic: String,
    val createdAt: String,
    val message: String,
    val read: Boolean,
    val targetType: String,
    val targetId: Long
)

@Serializable
data class ReadAllNotificationResDto(
    val message: String,
    val updatedCount: Int
)

@Serializable
data class DeleteAllNotificationResDto(
    val message: String,
    val deletedCount: Int
)
