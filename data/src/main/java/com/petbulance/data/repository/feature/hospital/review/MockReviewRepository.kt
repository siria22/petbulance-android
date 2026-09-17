package com.petbulance.data.repository.feature.hospital.review

import com.petbulance.data.repository.MockFixtures
import com.petbulance.data.repository.MockReview
import com.petbulance.domain.model.feature.hospital.review.HospitalInfoForReview
import com.petbulance.domain.model.feature.hospital.review.HospitalReview
import com.petbulance.domain.model.feature.hospital.review.ModifyReviewParam
import com.petbulance.domain.model.feature.hospital.review.MyReview
import com.petbulance.domain.model.feature.hospital.review.PagingReviewList
import com.petbulance.domain.model.feature.hospital.review.ReceiptAnalysisResult
import com.petbulance.domain.model.feature.hospital.review.ReviewDetail
import com.petbulance.domain.model.feature.hospital.review.ReviewSearchItem
import com.petbulance.domain.model.feature.hospital.review.ReviewStatus
import com.petbulance.domain.model.feature.hospital.review.SaveReviewParam
import com.petbulance.domain.model.feature.hospital.review.SaveReviewResult
import com.petbulance.domain.repository.feature.hospital.ReviewRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockReviewRepository @Inject constructor() : ReviewRepository {

    override suspend fun findHospital(name: String): Result<List<HospitalInfoForReview>> {
        val keyword = name.trim()
        return Result.success(
            MockFixtures.hospitals
                .filter { keyword.isEmpty() || it.name.contains(keyword) }
                .map { HospitalInfoForReview(it.id, it.name) }
        )
    }

    override suspend fun searchReview(
        query: String,
        cursorId: Long?,
        size: Int
    ): Result<PagingReviewList<ReviewSearchItem>> {
        delay(MockFixtures.NETWORK_DELAY_MS)
        val keyword = query.trim()
        val matched = MockFixtures.reviews.filter { review ->
            val hospitalName = MockFixtures.findHospital(review.hospitalId)?.name.orEmpty()
            keyword.isEmpty() ||
                    hospitalName.contains(keyword) ||
                    review.content.contains(keyword) ||
                    review.species.korean.contains(keyword)
        }
        return Result.success(matched.page(cursorId, size) { it.toSearchItem() })
    }

    /** 지역 필터는 Mock에서 적용하지 않는다. */
    override suspend fun filterReview(
        region: String?,
        animalTypes: List<String>?,
        isReceipt: Boolean?,
        cursorId: Long?,
        size: Int
    ): Result<PagingReviewList<ReviewSearchItem>> {
        delay(MockFixtures.NETWORK_DELAY_MS)
        val matched = MockFixtures.reviews.filter { review ->
            (animalTypes.isNullOrEmpty() || review.species.name in animalTypes) &&
                    (isReceipt != true || review.isReceiptVerified)
        }
        return Result.success(matched.page(cursorId, size) { it.toSearchItem() })
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
        delay(MockFixtures.NETWORK_DELAY_MS)
        // Mock 후기에는 사진이 없다.
        if (onlyImageReview) {
            return Result.success(PagingReviewList(items = emptyList(), nextCursorId = null, hasNext = false))
        }
        val sorted = when (sortBy) {
            SORT_LIKE_COUNT -> MockFixtures.reviewsOf(hospitalId).sortedByDescending { it.likeCount }
            SORT_TOTAL_RATING -> MockFixtures.reviewsOf(hospitalId).sortedByDescending { it.totalRating }
            else -> MockFixtures.reviewsOf(hospitalId).sortedByDescending { it.id }
        }
        return Result.success(sorted.page(cursorId, size) { it.toHospitalReview() })
    }

    override suspend fun saveReview(param: SaveReviewParam): Result<SaveReviewResult> {
        delay(MockFixtures.NETWORK_DELAY_MS)
        return Result.success(SaveReviewResult(reviewId = NEW_REVIEW_ID, uploadUrls = emptyList()))
    }

    override suspend fun checkReviewImageSave(
        reviewId: Long,
        keys: List<String>,
        type: String
    ): Result<String> = Result.success("Success")

    override suspend fun getMyReviews(
        size: Int,
        cursorId: Long?
    ): Result<PagingReviewList<MyReview>> {
        delay(MockFixtures.NETWORK_DELAY_MS)
        val mine = MockFixtures.reviews.filter { it.isMine }
        return Result.success(mine.page(cursorId, size) { it.toMyReview() })
    }

    override suspend fun modifyReview(param: ModifyReviewParam): Result<SaveReviewResult> {
        delay(MockFixtures.NETWORK_DELAY_MS)
        return Result.success(SaveReviewResult(reviewId = param.reviewId, uploadUrls = emptyList()))
    }

    override suspend fun deleteMyReviews(ids: List<Long>): Result<String> = Result.success("Deleted")

    override suspend fun analyzeReceipt(
        imageBytes: ByteArray,
        fileName: String
    ): Result<ReceiptAnalysisResult> {
        delay(MockFixtures.NETWORK_DELAY_MS)
        val sample = MockFixtures.reviews.first { it.isMine }
        val hospital = MockFixtures.findHospital(sample.hospitalId)
        return Result.success(
            ReceiptAnalysisResult(
                hospitalId = sample.hospitalId,
                hospitalName = hospital?.name.orEmpty(),
                visitDate = sample.visitDate(),
                totalPrice = sample.totalPrice.toLong(),
                items = sample.items
            )
        )
    }

    override suspend fun getReviewDetail(reviewId: Long): Result<ReviewDetail> {
        delay(MockFixtures.NETWORK_DELAY_MS)
        val review = MockFixtures.findReview(reviewId)
            ?: return Result.failure(NoSuchElementException("Review not found: $reviewId"))
        return Result.success(
            ReviewDetail(
                userNickname = review.writer,
                receiptCheck = review.isReceiptVerified,
                id = review.id,
                hospitalImage = null,
                hospitalId = review.hospitalId,
                hospitalName = review.hospitalName(),
                treatmentService = review.treatmentService,
                animalType = review.species.category,
                detailAnimalType = review.species,
                reviewContent = review.content,
                facilityRating = review.facilityRating,
                expertiseRating = review.expertiseRating,
                kindnessRating = review.kindnessRating,
                createDate = review.createdAt(),
                totalPrice = review.totalPrice,
                likeCount = review.likeCount,
                liked = false,
                visitDate = review.visitDate(),
                images = emptyList(),
                userProfileImage = null,
                viewCount = review.likeCount * VIEW_PER_LIKE,
                isAuthor = review.isMine
            )
        )
    }

    override suspend fun likeReview(reviewId: Long): Result<String> = Result.success("Like success")

    override suspend fun unlikeReview(reviewId: Long): Result<String> = Result.success("Unlike success")

    private fun MockReview.hospitalName(): String = MockFixtures.findHospital(hospitalId)?.name.orEmpty()

    private fun MockReview.toSearchItem() = ReviewSearchItem(
        userNickname = writer,
        receiptCheck = isReceiptVerified,
        id = id,
        hospitalImage = null,
        hospitalId = hospitalId,
        hospitalName = hospitalName(),
        treatmentService = treatmentService,
        animalType = species.category,
        detailAnimalType = species,
        reviewContent = content,
        totalRating = totalRating,
        createDate = createdAt(),
        totalPrice = totalPrice,
        likeCount = likeCount,
        liked = false,
        images = emptyList()
    )

    private fun MockReview.toHospitalReview() = HospitalReview(
        id = id,
        hospitalName = hospitalName(),
        isReceiptVerified = isReceiptVerified,
        treatment = treatmentService,
        animalType = species.category,
        detailAnimalType = species,
        content = content,
        rating = totalRating,
        date = createdAt(),
        likeCount = likeCount,
        isLiked = false,
        imageUrls = emptyList(),
        author = writer,
        price = totalPrice,
        isAuthor = isMine
    )

    private fun MockReview.toMyReview() = MyReview(
        id = id,
        hospitalName = hospitalName(),
        content = content,
        date = createdDate(),
        representativeImage = null,
        likeCount = likeCount,
        isReceiptVerified = isReceiptVerified,
        status = if (isUnderReview) ReviewStatus.UNDER_REVIEW else ReviewStatus.REGISTERED
    )

    /** 목록 순서를 유지한 채 cursorId 다음부터 size개를 잘라낸다. */
    private fun <T> List<MockReview>.page(
        cursorId: Long?,
        size: Int,
        transform: (MockReview) -> T
    ): PagingReviewList<T> {
        val startIndex = cursorId?.let { id -> indexOfFirst { it.id == id } + 1 } ?: 0
        val page = drop(startIndex).take(size)
        return PagingReviewList(
            items = page.map(transform),
            nextCursorId = page.lastOrNull()?.id,
            hasNext = startIndex + page.size < this.size
        )
    }

    companion object {
        private const val SORT_LIKE_COUNT = "likeCount"
        private const val SORT_TOTAL_RATING = "totalRating"
        private const val NEW_REVIEW_ID = 13L
        private const val VIEW_PER_LIKE = 7
    }
}
