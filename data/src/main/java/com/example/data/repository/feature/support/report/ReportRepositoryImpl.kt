package com.example.data.repository.feature.support.report

import com.example.data.datasource.remote.network.common.safeApiCall
import com.example.data.datasource.remote.network.feature.support.report.ReportApi
import com.example.data.datasource.remote.network.feature.support.report.dto.ReportCreateReqDto
import com.example.data.datasource.remote.network.feature.support.report.dto.ReportCreateResDto
import com.example.domain.model.feature.support.report.ReportParam
import com.example.domain.model.type.ReportType
import com.example.domain.repository.feature.support.ReportRepository
import javax.inject.Inject

class ReportRepositoryImpl @Inject constructor(
    private val api: ReportApi
) : ReportRepository {

    override suspend fun createReport(param: ReportParam): Result<Unit> {
        val reqDto = ReportCreateReqDto(
            reportType = param.reportType.name,
            reportReason = param.reportReason,
            postId = if (param.reportType == ReportType.POST) param.targetId else null,
            commentId = if (param.reportType == ReportType.COMMENT) param.targetId else null,
            reviewId = if (param.reportType == ReportType.REVIEW) param.targetId else null
        )

        return safeApiCall<ReportCreateResDto>("reports") {
            api.createReport(reqDto)
        }.map { }
    }
}