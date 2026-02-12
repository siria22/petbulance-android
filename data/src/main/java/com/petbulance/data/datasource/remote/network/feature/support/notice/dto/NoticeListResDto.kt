package com.petbulance.data.datasource.remote.network.feature.support.notice.dto

import kotlinx.serialization.Serializable

@Serializable
data class PagingNoticeListResDto(
    val content: List<NoticeListResDto>,
    val hasNext: Boolean
)

@Serializable
data class NoticeListResDto(
    val noticeId: Long,
    val noticeStatus: String,
    val title: String,
    val content: String?,
    val createdAt: String
)