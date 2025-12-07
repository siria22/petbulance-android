package com.example.domain.model.feature.hospital.review

data class MyReview(
    val id: Long,
    val hospitalName: String,
    val content: String,
    val date: String,
    val rating: Double,
    val representativeImage: String?
)