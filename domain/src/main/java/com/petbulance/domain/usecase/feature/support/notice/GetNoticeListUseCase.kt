package com.petbulance.domain.usecase.feature.support.notice

import com.petbulance.domain.model.feature.support.notice.PagingNoticeList
import com.petbulance.domain.repository.feature.support.NoticeRepository
import javax.inject.Inject

class GetNoticeListUseCase @Inject constructor(
    private val noticeRepository: NoticeRepository
) {
    suspend operator fun invoke(
        lastNoticeId: Long? = null,
        pageSize: Int = 10
    ): Result<PagingNoticeList> {
        return noticeRepository.getNoticeList(lastNoticeId, pageSize)
    }
}
