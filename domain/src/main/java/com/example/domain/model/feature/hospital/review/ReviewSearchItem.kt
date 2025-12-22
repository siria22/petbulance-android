package com.example.domain.model.feature.hospital.review

data class ReviewSearchItem(
    val id: Long,
    val hospitalName: String,
    val content: String,
    val rating: Double,
    val treatment: String,
    val isReceiptVerified: Boolean,
    val animalType: String,
    val totalReviewCount: Int
)