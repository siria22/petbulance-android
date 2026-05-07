package com.petbulance.data.mapper.feature.notification

import com.petbulance.data.datasource.remote.network.feature.notification.dto.DeleteAllNotificationResDto
import com.petbulance.data.datasource.remote.network.feature.notification.dto.NotificationResDto
import com.petbulance.data.datasource.remote.network.feature.notification.dto.PagingNotificationListResDto
import com.petbulance.data.datasource.remote.network.feature.notification.dto.ReadAllNotificationResDto
import com.petbulance.domain.model.feature.notification.DeleteAllResult
import com.petbulance.domain.model.feature.notification.NotificationItem
import com.petbulance.domain.model.feature.notification.PagingNotificationList
import com.petbulance.domain.model.feature.notification.ReadAllResult

fun PagingNotificationListResDto.toDomain() = PagingNotificationList(
    content = content.map { it.toDomain() },
    hasNext = hasNext,
    lastNotificationId = lastNotificationId
)

fun NotificationResDto.toDomain() = NotificationItem(
    notificationId = notificationId,
    type = type,
    topic = topic,
    createdAt = createdAt,
    message = message,
    isRead = read,
    targetType = targetType,
    targetId = targetId
)

fun ReadAllNotificationResDto.toDomain() = ReadAllResult(
    message = message,
    updatedCount = updatedCount
)

fun DeleteAllNotificationResDto.toDomain() = DeleteAllResult(
    message = message,
    deletedCount = deletedCount
)
