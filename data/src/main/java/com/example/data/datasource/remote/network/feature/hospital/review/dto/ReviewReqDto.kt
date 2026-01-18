package com.example.data.datasource.remote.network.feature.hospital.review.dto

import kotlinx.serialization.Serializable

// TODO : 체크
@Serializable
data class ReviewSaveReqDto(
    val hospitalId: Long,
    val expertiseRating: Double,
    val kindnessRating: Double,
    val facilityRating: Double,
    val totalPrice: Long,
    val animalType: String,
    val detailAnimalType: String,
    val receiptItems: List<ReceiptItemDto>,
    val visitDate: String,
    val reviewComment: String,
    val receiptChecked: Boolean,
    val title: String? = null,
    val images: List<ReviewImageDto>? = null
)

@Serializable
data class ReceiptItemDto(
    val name: String,
    val price: Int
)

@Serializable
data class ReviewImageDto(
    val filename: String,
    val contentType: String,
    val content: String,
    val receipt: Boolean
)

@Serializable
data class ReviewImageCheckReqDto(
    val reviewId: Long,
    val saveIds: List<String>
)