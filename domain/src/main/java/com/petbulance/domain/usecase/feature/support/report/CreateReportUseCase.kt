package com.petbulance.domain.usecase.feature.support.report

import com.petbulance.domain.model.feature.support.report.ReportParam
import com.petbulance.domain.repository.feature.support.ReportRepository
import javax.inject.Inject

class CreateReportUseCase @Inject constructor(
    private val repository: ReportRepository
) {
    suspend operator fun invoke(param: ReportParam): Result<Unit> {
        return repository.createReport(param)
    }
}