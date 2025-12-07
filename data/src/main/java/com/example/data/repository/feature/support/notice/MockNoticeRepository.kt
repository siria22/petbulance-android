package com.example.data.repository.feature.support.notice

import com.example.domain.model.feature.support.notice.NoticeDetail
import com.example.domain.model.feature.support.notice.PagingNoticeList
import com.example.domain.repository.feature.support.NoticeRepository

class MockNoticeRepository : NoticeRepository {
    override suspend fun getNoticeList(
        lastNoticeId: Long?,
        pageSize: Int
    ): Result<PagingNoticeList> {
        return Result.success(
            PagingNoticeList(
                content = emptyList(),
                hasNext = false
            )
        )
    }

    override suspend fun getNoticeDetail(noticeId: Long): Result<NoticeDetail> {
        return Result.success(
            NoticeDetail(
                noticeId = noticeId,
                title = "Mock Notice Title",
                content = "Mock Notice Content",
                createdAt = "2024-01-01",
                attachments = emptyList(),
                previousNotice = null,
                nextNotice = null,
                isImportant = false
            )
        )
    }
}