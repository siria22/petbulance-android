package com.example.domain.usecase.feature.hospital.review

import com.example.domain.model.feature.hospital.review.SaveReviewParam
import com.example.domain.repository.feature.hospital.ReviewRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class CreateReviewUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    suspend operator fun invoke(
        param: SaveReviewParam,
        images: List<ByteArray>
    ): Result<Unit> = coroutineScope {
        val saveResult = repository.saveReview(param).getOrElse {
            return@coroutineScope Result.failure(it)
        }

        val uploadUrls = saveResult.uploadUrls

        if (images.isEmpty() || uploadUrls.isEmpty()) {
            return@coroutineScope Result.success(Unit)
        }

        val uploadJobs = uploadUrls.zip(images).map { (urlInfo, imageBytes) ->
            async {
                repository.uploadImage(urlInfo.url, imageBytes)
                    .map { urlInfo.saveId }
                    .getOrNull()
            }
        }

        val uploadedKeys = uploadJobs.awaitAll().filterNotNull()

        if (uploadedKeys.isNotEmpty()) {
            repository.checkReviewImageSave(saveResult.reviewId, uploadedKeys)
                .map { Unit }
        } else {
            // TODO: 업로드 실패 시에도 리뷰는 생성된 상태. 실패 처리할지 성공 처리할지 정책에 따름.
            // 여기서는 이미지 업로드 실패로 간주
            Result.failure(Exception("이미지 업로드에 실패했습니다."))
        }
    }
}