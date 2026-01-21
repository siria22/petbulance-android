package com.petbulance.data.repository.feature.hospital.review

import com.petbulance.domain.model.feature.hospital.review.HospitalInfo
import com.petbulance.domain.model.feature.hospital.review.HospitalReview
import com.petbulance.domain.model.feature.hospital.review.MyReview
import com.petbulance.domain.model.feature.hospital.review.PagingReviewList
import com.petbulance.domain.model.feature.hospital.review.ReceiptAnalysisResult
import com.petbulance.domain.model.feature.hospital.review.ReviewSearchItem
import com.petbulance.domain.model.feature.hospital.review.SaveReviewParam
import com.petbulance.domain.model.feature.hospital.review.SaveReviewResult
import com.petbulance.domain.repository.feature.hospital.ReviewRepository
import javax.inject.Inject

class MockReviewRepository @Inject constructor() : ReviewRepository {

    override suspend fun findHospital(name: String): Result<List<HospitalInfo>> {
        return Result.success(
            listOf(
                HospitalInfo(1, "행복 동물병원"),
                HospitalInfo(2, "튼튼 동물병원")
            )
        )
    }

    override suspend fun searchReview(
        query: String,
        cursorId: Long?,
        size: Int
    ): Result<PagingReviewList<ReviewSearchItem>> {
        val reviews = listOf(
            ReviewSearchItem(
                1, "행복 동물병원", "친절하고 좋아요", 4.5,
                "예방접종", true, "강아지", 25
            ),
            ReviewSearchItem(
                2, "튼튼 동물병원", "시설이 깨끗해요", 5.0,
                "중성화 수술", false, "고양이", 15
            )
        )
        return Result.success(
            PagingReviewList(
                items = reviews,
                nextCursorId = reviews.last().id,
                hasNext = false
            )
        )
    }

    override suspend fun filterReview(
        region: String?,
        animalType: String?,
        isReceipt: Boolean?,
        cursorId: Long?,
        size: Int
    ): Result<PagingReviewList<ReviewSearchItem>> {
        val reviews = listOf(
            ReviewSearchItem(
                1, "행복 동물병원", "친절하고 좋아요",
                4.5, "예방접종", true, "강아지", 25
            ),
            ReviewSearchItem(
                2, "튼튼 동물병원", "시설이 깨끗해요",
                5.0, "중성화 수술", false, "고양이", 15
            )
        )
        return Result.success(
            PagingReviewList(
                items = reviews,
                nextCursorId = reviews.last().id,
                hasNext = false
            )
        )
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
        val reviews = listOf(
            HospitalReview(
                id = 1,
                hospitalName = "행복동물병원",
                isReceiptVerified = true,
                treatment = "예방접종",
                animalType = "어류",
                detailAnimalType = "금붕어",
                content = "휴! 행복해지네요 ㅎㅎ",
                rating = 4.2,
                date = "2024-11-20",
                likeCount = 11,
                isLiked = false,
                imageUrls = listOf(),
                author = "내가 썼어요",
                price = 100_000
            ),
            HospitalReview(
                id = 2,
                hospitalName = "행복동물병원",
                isReceiptVerified = true,
                treatment = "예방접종",
                animalType = "어류",
                detailAnimalType = "금붕어",
                content = "휴! 행복해지네요 ㅎㅎ",
                rating = 4.2,
                date = "2024-11-20",
                likeCount = 11,
                isLiked = false,
                imageUrls = listOf(),
                author = "내가 썼어요",
                price = 100_000
            )
        )
        return Result.success(
            PagingReviewList(
                items = reviews,
                nextCursorId = reviews.last().id,
                hasNext = false
            )
        )
    }

    override suspend fun saveReview(param: SaveReviewParam): Result<SaveReviewResult> {
        return Result.success(SaveReviewResult(reviewId = 1, uploadUrls = emptyList()))
    }

    override suspend fun checkReviewImageSave(reviewId: Long, keys: List<String>): Result<String> {
        return Result.success("Success")
    }

    override suspend fun getMyReviews(
        size: Int,
        cursorId: Long?
    ): Result<PagingReviewList<MyReview>> {
        val myReviews = listOf(
            MyReview(1, "행복 동물병원", "내 강아지가 좋아해요", "2023-11-20", 4.5, "url1"),
            MyReview(2, "튼튼 동물병원", "고양이 전문 병원!", "2023-11-19", 5.0, null)
        )
        return Result.success(
            PagingReviewList(
                items = myReviews,
                nextCursorId = myReviews.last().id,
                hasNext = false
            )
        )
    }

    override suspend fun deleteMyReviews(ids: List<Long>): Result<String> {
        return Result.success("Deleted")
    }

    override suspend fun analyzeReceipt(
        imageBytes: ByteArray,
        fileName: String
    ): Result<ReceiptAnalysisResult> {
        return Result.success(
            ReceiptAnalysisResult(
                hospitalName = "행복 동물병원",
                visitDate = "2023-11-20",
                items = emptyList(),
                hospitalId = 0L,
                totalPrice = 120_000
            )
        )
    }

    override suspend fun uploadImage(
        url: String,
        imageBytes: ByteArray
    ): Result<Unit> {
        return Result.success(Unit)
    }
}