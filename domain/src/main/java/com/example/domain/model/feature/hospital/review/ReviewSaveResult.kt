package com.example.domain.model.feature.hospital.review

data class ReviewSaveResult(
    val reviewId: Long,
    val uploadUrls: List<ReviewUploadUrl>
)

data class ReviewUploadUrl(
    val url: String,
    val saveId: String
)