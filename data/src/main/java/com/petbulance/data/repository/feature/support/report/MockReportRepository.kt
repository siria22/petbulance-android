package com.petbulance.data.repository.feature.support.report

import com.petbulance.domain.model.feature.support.report.ReportParam
import com.petbulance.domain.repository.feature.support.ReportRepository
import javax.inject.Inject

class MockReportRepository @Inject constructor() : ReportRepository {
    override suspend fun createReport(param: ReportParam): Result<Unit> = Result.success(Unit)
}