package com.petbulance.domain.model.feature.support.notice

import com.petbulance.domain.model.type.NoticeStatusType

data class PagingNoticeList(
    val content: List<NoticeListItem>,
    val hasNext: Boolean
)

data class NoticeListItem(
    val noticeId: Long,
    val noticeStatus: NoticeStatusType,
    val title: String,
    val content: String?,
    val createdAt: String
) {
    companion object {

        fun stub() = NoticeListItem(
            noticeId = 0,
            noticeStatus = NoticeStatusType.NOTICE,
            title = "Stub Title",
            content = "Stub Content",
            createdAt = "Stub Created At"
        )

        fun stubs() = listOf(
            NoticeListItem(
                noticeId = 0,
                noticeStatus = NoticeStatusType.NOTICE,
                title = "Stub Title",
                content = "Stub Content",
                createdAt = "Stub Created At"
            ),
            NoticeListItem(
                noticeId = 0,
                noticeStatus = NoticeStatusType.EVENT,
                title = "Stub Title",
                content = "Stub Content",
                createdAt = "Stub Created At"
            ),
            NoticeListItem(
                noticeId = 0,
                noticeStatus = NoticeStatusType.ADVERTISING,
                title = "Stub Title",
                content = "Stub Content",
                createdAt = "Stub Created At"
            ),
        )
    }
}