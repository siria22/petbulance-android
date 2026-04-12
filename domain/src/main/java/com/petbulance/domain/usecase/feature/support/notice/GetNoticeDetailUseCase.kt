package com.petbulance.domain.usecase.feature.support.notice

import com.petbulance.domain.model.feature.support.notice.NoticeDetail
import com.petbulance.domain.repository.feature.support.NoticeRepository
import javax.inject.Inject

class GetNoticeDetailUseCase @Inject constructor(
    private val noticeRepository: NoticeRepository
) {
    suspend operator fun invoke(noticeId: Long): Result<NoticeDetail> {
        return noticeRepository.getNoticeDetail(noticeId)
    }
}
