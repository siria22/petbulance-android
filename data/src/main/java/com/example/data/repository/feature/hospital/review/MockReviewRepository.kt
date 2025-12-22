package com.example.data.repository.feature.hospital.review

import com.example.domain.model.feature.hospital.review.HospitalInfo
import com.example.domain.model.feature.hospital.review.HospitalReview
import com.example.domain.model.feature.hospital.review.MyReview
import com.example.domain.model.feature.hospital.review.PagingReviewList
import com.example.domain.model.feature.hospital.review.ReviewSearchItem
import com.example.domain.model.feature.hospital.review.SaveReviewParam
import com.example.domain.model.feature.hospital.review.SaveReviewResult
import com.example.domain.repository.feature.hospital.ReviewRepository
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
                1,
                true,
                "예방접종",
                "강아지",
                "푸들",
                "친절해요",
                4.5,
                "2023-11-20",
                10,
                true,
                listOf("url1", "url2"),
                author = "게코매니아",
                price = 48_000
            ),
            HospitalReview(
                2,
                false,
                "중성화",
                "고양이",
                "코리안숏헤어",
                "꼼꼼해요",
                5.0,
                "2023-11-19",
                5,
                false,
                emptyList(),
                author = "앵무새조와",
                price = 50_000
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
}