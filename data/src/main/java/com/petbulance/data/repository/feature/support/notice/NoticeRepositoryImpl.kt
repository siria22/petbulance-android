package com.petbulance.data.repository.feature.support.notice

import com.petbulance.data.datasource.remote.network.feature.support.notice.NoticeApi
import com.petbulance.data.datasource.remote.network.feature.support.notice.dto.DetailNoticeResDto
import com.petbulance.data.datasource.remote.network.feature.support.notice.dto.PagingNoticeListResDto
import com.petbulance.data.datasource.remote.network.common.safeApiCall
import com.petbulance.data.mapper.feature.support.toDomain
import com.petbulance.domain.model.feature.support.notice.NoticeDetail
import com.petbulance.domain.model.feature.support.notice.PagingNoticeList
import com.petbulance.domain.repository.feature.support.NoticeRepository
import javax.inject.Inject

class NoticeRepositoryImpl @Inject constructor(
    private val api: NoticeApi
) : NoticeRepository {
    override suspend fun getNoticeList(
        lastNoticeId: Long?,
        pageSize: Int
    ): Result<PagingNoticeList> {
        return safeApiCall<PagingNoticeListResDto>(path = "/notices") {
            api.getNoticeList(lastNoticeId, pageSize)
        }.map { it.toDomain() }
    }

    override suspend fun getNoticeDetail(noticeId: Long): Result<NoticeDetail> {
        return safeApiCall<DetailNoticeResDto>(path = "/notices/$noticeId") {
            api.getNoticeDetail(noticeId)
        }.map { it.toDomain() }
    }
}