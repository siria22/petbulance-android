package com.petbulance.domain.model.feature.support.notice

import com.petbulance.domain.model.type.NoticeStatusType

data class NoticeDetail(
    val noticeId: Long,
    val noticeStatus: NoticeStatusType,
    val title: String,
    val createdAt: String,
    val content: String,
    val attachments: List<Attachment> = emptyList(),
    val previousNotice: AdjacentNotice? = null,
    val nextNotice: AdjacentNotice? = null,
    val buttons: List<NoticeButton>? = null
) {
    val hasButtons: Boolean
        get() = !buttons.isNullOrEmpty()
}

data class Attachment(
    val fileId: Long,
    val fileName: String,
    val fileUrl: String,
    val fileType: String
)

data class AdjacentNotice(
    val noticeId: Long,
    val title: String
)