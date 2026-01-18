package com.example.domain.repository.feature.hospital

import com.example.domain.model.feature.hospital.review.HospitalInfo
import com.example.domain.model.feature.hospital.review.HospitalReview
import com.example.domain.model.feature.hospital.review.MyReview
import com.example.domain.model.feature.hospital.review.PagingReviewList
import com.example.domain.model.feature.hospital.review.ReceiptAnalysisResult
import com.example.domain.model.feature.hospital.review.SaveReviewResult
import com.example.domain.model.feature.hospital.review.ReviewSearchItem
import com.example.domain.model.feature.hospital.review.SaveReviewParam

interface ReviewRepository {

    suspend fun findHospital(name: String): Result<List<HospitalInfo>>

    suspend fun searchReview(
        query: String,
        cursorId: Long?,
        size: Int = 10
    ): Result<PagingReviewList<ReviewSearchItem>>

    suspend fun filterReview(
        region: String?,
        animalType: String?,
        isReceipt: Boolean?,
        cursorId: Long?,
        size: Int = 10
    ): Result<PagingReviewList<ReviewSearchItem>>

    suspend fun getHospitalReviews(
        hospitalId: Long,
        onlyImageReview: Boolean = false,
        cursorId: Long? = null,
        cursorRating: Double? = null,
        cursorLikeCount: Long? = null,
        size: Int = 10,
        sortBy: String = "createdAt",
        sortDirection: String = "desc"
    ): Result<PagingReviewList<HospitalReview>>

    suspend fun saveReview(param: SaveReviewParam): Result<SaveReviewResult>

    suspend fun checkReviewImageSave(reviewId: Long, keys: List<String>): Result<String>

    suspend fun getMyReviews(
        size: Int = 10,
        cursorId: Long? = null
    ): Result<PagingReviewList<MyReview>>

    suspend fun deleteMyReviews(ids: List<Long>): Result<String>

    suspend fun analyzeReceipt(imageBytes: ByteArray, fileName: String): Result<ReceiptAnalysisResult>

    suspend fun uploadImage(url: String, imageBytes: ByteArray): Result<Unit>
}