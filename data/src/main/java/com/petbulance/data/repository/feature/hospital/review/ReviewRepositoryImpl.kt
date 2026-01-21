package com.petbulance.data.repository.feature.hospital.review

import com.petbulance.data.datasource.remote.network.common.safeApiCall
import com.petbulance.data.datasource.remote.network.feature.hospital.review.ReviewApi
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.CursorPagingResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.FilterResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.FindHospitalResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.HospitalReviewsCursorResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.MyReviewGetResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.ReceiptAnalysisResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.ReviewDeleteResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.ReviewImageCheckReqDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.ReviewImageCheckResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.ReviewSaveResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.review.dto.UserReviewSearchDto
import com.petbulance.data.mapper.feature.hospital.toDomain
import com.petbulance.data.mapper.feature.hospital.toDto
import com.petbulance.domain.model.feature.hospital.review.HospitalInfo
import com.petbulance.domain.model.feature.hospital.review.HospitalReview
import com.petbulance.domain.model.feature.hospital.review.MyReview
import com.petbulance.domain.model.feature.hospital.review.PagingReviewList
import com.petbulance.domain.model.feature.hospital.review.ReceiptAnalysisResult
import com.petbulance.domain.model.feature.hospital.review.SaveReviewResult
import com.petbulance.domain.model.feature.hospital.review.ReviewSearchItem
import com.petbulance.domain.model.feature.hospital.review.ReviewUploadUrl
import com.petbulance.domain.model.feature.hospital.review.SaveReviewParam
import com.petbulance.domain.repository.feature.hospital.ReviewRepository
import javax.inject.Inject

class ReviewRepositoryImpl @Inject constructor(
    private val api: ReviewApi
) : ReviewRepository {

    override suspend fun findHospital(name: String): Result<List<HospitalInfo>> {
        return safeApiCall<FindHospitalResDto>(path = "/receipts/$name") {
            api.findHospital(name)
        }.map { dto ->
            dto.hospitals.map { HospitalInfo(it.hospitalId, it.hospitalName) }
        }
    }

    override suspend fun searchReview(
        query: String,
        cursorId: Long?,
        size: Int
    ): Result<PagingReviewList<ReviewSearchItem>> {
        return safeApiCall<CursorPagingResDto<UserReviewSearchDto>>(path = "/receipts/search/$query") {
            api.searchReview(query, cursorId, size)
        }.map { dto ->
            PagingReviewList(
                items = dto.list.map { it.toDomain() },
                nextCursorId = dto.nextCursorId,
                hasNext = dto.hasNext
            )
        }
    }

    override suspend fun filterReview(
        region: String?,
        animalType: String?,
        isReceipt: Boolean?,
        cursorId: Long?,
        size: Int
    ): Result<PagingReviewList<ReviewSearchItem>> {
        return safeApiCall<CursorPagingResDto<FilterResDto>>(path = "/receipts/filter") {
            api.filterReview(region, animalType, isReceipt, cursorId, size)
        }.map { dto ->
            PagingReviewList(
                items = dto.list.map { it.toDomain() },
                nextCursorId = dto.nextCursorId,
                hasNext = dto.hasNext
            )
        }
    }

    override suspend fun getHospitalReviews(
        hospitalId: Long,
        onlyImageReview: Boolean,
        cursorId: Long?,
        cursorRating: Double?,
        cursorLikeCount: Long?,
        size: Int,
        sortBy: String,
        sortDirection: String
    ): Result<PagingReviewList<HospitalReview>> {
        return safeApiCall<HospitalReviewsCursorResDto>(path = "/receipts/reviews/$hospitalId") {
            api.getHospitalReviews(
                hospitalId,
                onlyImageReview,
                cursorId,
                cursorRating,
                cursorLikeCount,
                size,
                sortBy,
                sortDirection
            )
        }.map { dto ->
            PagingReviewList(
                items = dto.list.map { it.toDomain() },
                nextCursorId = dto.nextCursorId,
                hasNext = dto.hasNext
            )
        }
    }

    override suspend fun saveReview(param: SaveReviewParam): Result<SaveReviewResult> {
        return safeApiCall<ReviewSaveResDto>(path = "/receipts/save/reviews") {
            api.saveReview(param.toDto())
        }.map { dto ->
            SaveReviewResult(
                reviewId = dto.reviewId,
                uploadUrls = dto.urls.map { ReviewUploadUrl(it.presignedUrl, it.saveId) }
            )
        }
    }

    override suspend fun checkReviewImageSave(reviewId: Long, keys: List<String>): Result<String> {
        return safeApiCall<ReviewImageCheckResDto>(path = "/receipts/save/success") {
            val reqDto = ReviewImageCheckReqDto(
                reviewId = reviewId,
                saveIds = keys
            )
            api.checkReviewImageSave(reqDto)
        }.map { it.message }
    }

    override suspend fun getMyReviews(
        size: Int,
        cursorId: Long?
    ): Result<PagingReviewList<MyReview>> {
        return safeApiCall<MyReviewGetResDto>(path = "/receipts/me") {
            api.getMyReviews(size, cursorId)
        }.map { dto ->
            PagingReviewList(
                items = dto.list.map { it.toDomain() },
                nextCursorId = dto.nextCursorId,
                hasNext = dto.hasNext
            )
        }
    }

    override suspend fun deleteMyReviews(ids: List<Long>): Result<String> {
        return safeApiCall<ReviewDeleteResDto>(path = "/receipts") {
            api.deleteMyReviews(ids)
        }.map { it.message }
    }

    override suspend fun analyzeReceipt(imageBytes: ByteArray, fileName: String): Result<ReceiptAnalysisResult> {
        return safeApiCall<ReceiptAnalysisResDto>(path = "/receipts") {
            api.analyzeReceipt(imageBytes, fileName)
        }.map { it.toDomain() }
    }

    override suspend fun uploadImage(url: String, imageBytes: ByteArray): Result<Unit> {
        return safeApiCall<Unit>(path = url) {
            api.uploadImage(url, imageBytes)
        }.map { }
    }
}