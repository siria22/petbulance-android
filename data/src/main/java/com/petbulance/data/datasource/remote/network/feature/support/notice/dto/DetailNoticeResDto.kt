package com.petbulance.data.datasource.remote.network.feature.support.notice.dto

import kotlinx.serialization.Serializable

@Serializable
data class DetailNoticeResDto(
    val noticeId: Long,
    val noticeStatus: String,
    val title: String,
    val createdAt: String,
    val content: String,
    val attachments: List<AttachmentDto> = emptyList(),
    val previousNotice: AdjacentNoticeDto? = null,
    val nextNotice: AdjacentNoticeDto? = null,
    val buttons: List<NoticeButtonDto>? = null
)

@Serializable
data class AttachmentDto(
    val fileId: Long,
    val fileName: String,
    val fileUrl: String,
    val fileType: String? = null
)

@Serializable
data class AdjacentNoticeDto(
    val noticeId: Long,
    val title: String
)