package com.petbulance.domain.repository.feature.support

import com.petbulance.domain.model.feature.support.report.ReportParam

interface ReportRepository {
    suspend fun createReport(param: ReportParam): Result<Unit>
}