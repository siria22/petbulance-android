package com.example.domain.model.feature.hospital.review

data class HospitalReview(
    val id: Long,
    val isReceiptVerified: Boolean,
    val treatment: String,
    val animalType: String,
    val detailAnimalType: String,
    val content: String,
    val rating: Double,
    val date: String,
    val likeCount: Int,
    val isLiked: Boolean,
    val imageUrls: List<String>
)