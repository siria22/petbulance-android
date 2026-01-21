package com.petbulance.domain.repository.feature.support

import com.petbulance.domain.model.feature.support.notice.NoticeDetail
import com.petbulance.domain.model.feature.support.notice.PagingNoticeList

interface NoticeRepository {
    suspend fun getNoticeList(
        lastNoticeId: Long? = null,
        pageSize: Int = 10
    ): Result<PagingNoticeList>

    suspend fun getNoticeDetail(noticeId: Long): Result<NoticeDetail>
}