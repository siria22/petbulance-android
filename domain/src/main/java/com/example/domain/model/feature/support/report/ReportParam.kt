package com.example.domain.model.feature.support.report

import com.example.domain.model.type.ReportType

data class ReportParam(
    val reportType: ReportType,
    val reportReason: String,
    val targetId: Long
)