package com.petbulance.domain.repository.feature.hospital

import com.petbulance.domain.model.feature.hospital.review.HospitalInfoForReview
import com.petbulance.domain.model.feature.hospital.review.HospitalReview
import com.petbulance.domain.model.feature.hospital.review.ModifyReviewParam
import com.petbulance.domain.model.feature.hospital.review.MyReview
import com.petbulance.domain.model.feature.hospital.review.PagingReviewList
import com.petbulance.domain.model.feature.hospital.review.ReceiptAnalysisResult
import com.petbulance.domain.model.feature.hospital.review.ReviewDetail
import com.petbulance.domain.model.feature.hospital.review.SaveReviewResult
import com.petbulance.domain.model.feature.hospital.review.ReviewSearchItem
import com.petbulance.domain.model.feature.hospital.review.SaveReviewParam

interface ReviewRepository {

    suspend fun findHospital(name: String): Result<List<HospitalInfoForReview>>

    suspend fun searchReview(
        query: String,
        cursorId: Long?,
        size: Int = 10
    ): Result<PagingReviewList<ReviewSearchItem>>

    suspend fun filterReview(
        region: String?,
        animalTypes: List<String>?,
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

    suspend fun modifyReview(param: ModifyReviewParam): Result<SaveReviewResult>

    suspend fun deleteMyReviews(ids: List<Long>): Result<String>

    suspend fun analyzeReceipt(imageBytes: ByteArray, fileName: String): Result<ReceiptAnalysisResult>

    suspend fun getReviewDetail(reviewId: Long): Result<ReviewDetail>
}