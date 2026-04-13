package com.petbulance.presentation.screen.feature.notification

import com.petbulance.domain.model.feature.notification.NotificationItem
import com.petbulance.domain.model.feature.support.notice.NoticeListItem

data class NotificationData(
    val selectedTab: NotificationTab = NotificationTab.NOTICE,
    val notices: List<NoticeListItem> = emptyList(),
    val isNoticeLoadingNextPage: Boolean = false,
    val notifications: List<NotificationItem> = emptyList(),
    val isNotificationLoadingNextPage: Boolean = false
) {
    companion object {
        val empty = NotificationData()
    }
}
