package com.petbulance.domain.model.feature.support.notice


data class PagingNoticeList(
    val content: List<NoticeListItem>,
    val hasNext: Boolean
)

data class NoticeListItem(
    val noticeId: Long,
    val isImportant: Boolean,
    val title: String,
    val content: String?,
    val createdAt: String
)
