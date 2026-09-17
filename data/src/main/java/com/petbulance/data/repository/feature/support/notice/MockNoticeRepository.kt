package com.petbulance.data.repository.feature.support.notice

import com.petbulance.data.repository.MockFixtures
import com.petbulance.data.repository.MockNotice
import com.petbulance.domain.model.feature.support.notice.AdjacentNotice
import com.petbulance.domain.model.feature.support.notice.NoticeDetail
import com.petbulance.domain.model.feature.support.notice.NoticeListItem
import com.petbulance.domain.model.feature.support.notice.PagingNoticeList
import com.petbulance.domain.repository.feature.support.NoticeRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockNoticeRepository @Inject constructor() : NoticeRepository {

    override suspend fun getNoticeList(
        lastNoticeId: Long?,
        pageSize: Int
    ): Result<PagingNoticeList> {
        delay(MockFixtures.NETWORK_DELAY_MS)
        val sorted = MockFixtures.notices.sortedByDescending { it.id }
        val startIndex = lastNoticeId?.let { id -> sorted.indexOfFirst { it.id == id } + 1 } ?: 0
        val page = sorted.drop(startIndex).take(pageSize)

        return Result.success(
            PagingNoticeList(
                content = page.map { notice ->
                    NoticeListItem(
                        noticeId = notice.id,
                        noticeStatus = notice.status,
                        title = notice.title,
                        content = notice.content,
                        createdAt = MockFixtures.dateLabel(notice.daysAgo)
                    )
                },
                hasNext = startIndex + page.size < sorted.size
            )
        )
    }

    override suspend fun getNoticeDetail(noticeId: Long): Result<NoticeDetail> {
        delay(MockFixtures.NETWORK_DELAY_MS)
        val notice = MockFixtures.findNotice(noticeId)
            ?: return Result.failure(NoSuchElementException("Notice not found: $noticeId"))

        return Result.success(
            NoticeDetail(
                noticeId = notice.id,
                noticeStatus = notice.status,
                title = notice.title,
                createdAt = MockFixtures.dateLabel(notice.daysAgo),
                content = notice.content,
                attachments = emptyList(),
                previousNotice = MockFixtures.findNotice(notice.id - 1)?.toAdjacent(),
                nextNotice = MockFixtures.findNotice(notice.id + 1)?.toAdjacent()
            )
        )
    }

    private fun MockNotice.toAdjacent() = AdjacentNotice(noticeId = id, title = title)
}
