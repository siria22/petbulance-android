package com.petbulance.data.datasource.remote.network.feature.hospital.review.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReviewDetailResDto(
    val userNickname: String? = null,
    val receiptCheck: Boolean,
    val id: Long,
    val hospitalImage: String? = null,
    val hospitalId: Long,
    val hospitalName: String,
    val treatmentService: String,
    val animalType: String,
    val detailAnimalType: String? = null,
    val reviewContent: String,
    val facilityRating: Double,
    val expertiseRating: Double,
    val kindnessRating: Double,
    val createDate: String,
    val totalPrice: Int,
    val likeCount: Int,
    val liked: Boolean,
    val visitDate: String? = null,
    val images: List<String>? = null,
    val userProfileImage: String? = null,
    val viewCount: Int = 0,
)