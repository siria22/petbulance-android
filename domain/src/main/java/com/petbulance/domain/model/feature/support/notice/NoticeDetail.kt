package com.petbulance.domain.model.feature.support.notice


data class NoticeDetail(
    val noticeId: Long,
    val isImportant: Boolean,
    val title: String,
    val createdAt: String,
    val content: String,
    val attachments: List<Attachment> = emptyList(),
    val previousNotice: AdjacentNotice? = null,
    val nextNotice: AdjacentNotice? = null
)
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