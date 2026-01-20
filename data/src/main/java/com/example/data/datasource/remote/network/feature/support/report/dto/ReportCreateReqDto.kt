package com.example.data.datasource.remote.network.feature.support.report.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReportCreateReqDto(
    val reportType: String, // POST, COMMENT, REVIEW
    val reportReason: String,
    val postId: Long? = null,
    val commentId: Long? = null,
    val reviewId: Long? = null
)