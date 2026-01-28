package com.petbulance.data.datasource.remote.network.feature.hospital.review.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReviewModifyReqDto(
    val reviewId: Long,
    val title: String, // 명세에 포함됨
    val receiptChecked: Boolean,
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
    val images: List<ReviewImageDto>? = null // FIXME : 최종적으로 올라갈 이미지
)