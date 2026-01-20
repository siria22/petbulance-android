package com.example.data.datasource.remote.network.feature.hospital.review.dto

import kotlinx.serialization.SerialName
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
    val receiptCheck: Boolean = false,
    val treatmentService: String,
    val animalType: String,
    val detailAnimalType: String,
    val reviewContent: String,
    val totalRating: Double,
    val reviewDate: String,
    val likeCount: Int = 0,
    val liked: Boolean = false,
    val images: List<String> = emptyList(),
    val author: String = "알 수 없음",
    val totalPrice: Int = 0
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
    val list: List<MyReviewGetDao>,
    val nextCursorId: Long?,
    val hasNext: Boolean
)

@Serializable
data class MyReviewGetDao(
    val id: Long,
    val hospitalName: String,
    val content: String,
    val createdAt: String,
    val rating: Double,
    val images: List<String> = emptyList()
)

@Serializable
data class ReviewDeleteResDto(
    val message: String
)