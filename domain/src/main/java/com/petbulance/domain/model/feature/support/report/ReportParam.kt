package com.petbulance.domain.model.feature.support.report

import com.petbulance.domain.model.type.ReportType

data class ReportParam(
    val reportType: ReportType,
    val reportReason: String,
    val targetId: Long
)