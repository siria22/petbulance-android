package com.petbulance.data.datasource.remote.network.feature.hospital.review.dto

import kotlinx.serialization.Serializable

@Serializable
data class CursorPagingResDto<T>(
    val list: List<T>,
    val nextCursorId: Long?,
    val hasNext: Boolean
)

@Serializable
data class FindHospitalResDto(
    val hospitals: List<HospitalDto>
)

@Serializable
data class HospitalDto(
    val hospitalId: Long,
    val hospitalName: String
)

@Serializable
data class UserReviewSearchDto(
    val receiptCheck: Boolean,
    val id: Long,
    val hospitalImage: String? = null,
    val hospitalId: Long,
    val hospitalName: String,
    val treatmentService: String,
    val detailAnimalType: String,
    val reviewContent: String,
    val overallRating: Double
)

@Serializable
data class FilterResDto(
    val receiptCheck: Boolean,
    val id: Long,
    val hospitalImage: String? = null,
    val hospitalId: Long,
    val hospitalName: String,
    val treatmentService: String,
    val detailAnimalType: String,
    val reviewContent: String,
    val totalRating: Double,
    val totalReviewCount: Int
)

@Serializable
data class HospitalReviewsCursorResDto(
    val list: List<SearchResDto>,
    val nextCursorId: Long?,
    val nextCursorRating: Double? = null,
    val nextCursorLikeCount: Int? = null,
    val hasNext: Boolean
)

@Serializable
data class SearchResDto(
    val id: Long,
    val hospitalName: String,
    val isReceiptVerified: Boolean,
    val treatment: String,
    val animalType: String,
    val detailAnimalType: String,
    val content: String,
    val rating: Double,
    val date: String,
    val likeCount: Int,
    val isLiked: Boolean,
    val imageUrls: List<String>,
    val author: String,
    val price: Int
)

@Serializable
data class ReviewSaveResDto(
    val reviewId: Long,
    val urls: List<UrlAndId>
)

@Serializable
data class UrlAndId(
    val presignedUrl: String,
    val saveId: String
)

@Serializable
data class ReviewImageCheckResDto(
    val message: String
)

@Serializable
data class MyReviewGetResDto(
    val list: List<MyReviewGetDto>,
    val nextCursorId: Long?,
    val hasNext: Boolean
)

@Serializable
data class MyReviewGetDto(
    val id: Long,
    val hospitalName: String,
    val hospitalImageUrl: String? = null,
    val reviewDate: String,
    val receiptChecked: Boolean,
    val likeCount: Int,
    val comment: String
)

@Serializable
data class ReviewDeleteResDto(
    val message: String
)