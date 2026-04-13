package com.petbulance.domain.model.feature.notification

data class PagingNotificationList(
    val content: List<NotificationItem>,
    val hasNext: Boolean,
    val lastNotificationId: Long?
)

data class NotificationItem(
    val notificationId: Long,
    val type: String,
    val topic: String,
    val createdAt: String,
    val message: String,
    val isRead: Boolean,
    val targetType: String,
    val targetId: Long
)

data class ReadAllResult(val message: String, val updatedCount: Int)
data class DeleteAllResult(val message: String, val deletedCount: Int)
