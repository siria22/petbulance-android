package com.example.domain.repository.feature.support

import com.example.domain.model.feature.support.report.ReportParam

interface ReportRepository {
    suspend fun createReport(param: ReportParam): Result<Unit>
}