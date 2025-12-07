package com.example.domain.repository.feature.hospital

import com.example.domain.model.feature.hospital.review.HospitalInfo
import com.example.domain.model.feature.hospital.review.HospitalReview
import com.example.domain.model.feature.hospital.review.MyReview
import com.example.domain.model.feature.hospital.review.PagingReviewList
import com.example.domain.model.feature.hospital.review.ReviewSaveResult
import com.example.domain.model.feature.hospital.review.ReviewSearchItem
import com.example.domain.model.feature.hospital.review.SaveReviewParam

interface ReviewRepository {

    // 1. 병원 검색
    suspend fun findHospital(name: String): Result<List<HospitalInfo>>

    // 2. 리뷰 검색 (키워드)
    suspend fun searchReview(
        query: String,
        cursorId: Long?,
        size: Int = 10
    ): Result<PagingReviewList<ReviewSearchItem>>

    // 3. 리뷰 필터
    suspend fun filterReview(
        region: String?,
        animalType: String?,
        isReceipt: Boolean?,
        cursorId: Long?,
        size: Int = 10
    ): Result<PagingReviewList<ReviewSearchItem>> // 검색 결과와 구조가 비슷하여 공유하거나 별도 모델 사용

    // 4. 병원 상세 리뷰 조회
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

    // 5. 리뷰 저장
    suspend fun saveReview(param: SaveReviewParam): Result<ReviewSaveResult>

    // 6. 이미지 저장 확인
    suspend fun checkReviewImageSave(reviewId: Long, keys: List<String>): Result<String>

    // 7. 내 리뷰 조회
    suspend fun getMyReviews(
        size: Int = 10,
        cursorId: Long? = null
    ): Result<PagingReviewList<MyReview>>

    // 8. 내 리뷰 삭제
    suspend fun deleteMyReviews(ids: List<Long>): Result<String>
}