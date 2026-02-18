package com.petbulance.domain.usecase.feature.hospital.review

import com.petbulance.domain.model.feature.hospital.review.ModifyReviewParam
import com.petbulance.domain.repository.feature.hospital.ReviewRepository
import com.petbulance.domain.usecase.nonfeature.app.UploadImageUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class ModifyReviewUseCase @Inject constructor(
    private val repository: ReviewRepository,
    private val uploadImage: UploadImageUseCase
) {
    suspend operator fun invoke(
        param: ModifyReviewParam,
        newImageBytes: List<ByteArray>
    ): Result<Unit> = coroutineScope {
        // 1. 리뷰 수정 요청 & 업로드 URL 획득
        val saveResult = repository.modifyReview(param).getOrElse {
            return@coroutineScope Result.failure(it)
        }

        val uploadUrls = saveResult.uploadUrls

        // 추가할 이미지가 없거나 URL이 없으면 종료
        if (newImageBytes.isEmpty() || uploadUrls.isEmpty()) {
            return@coroutineScope Result.success(Unit)
        }

        // 2. Presigned URL을 통한 이미지 업로드
        val uploadJobs = uploadUrls.zip(newImageBytes).map { (urlInfo, imageBytes) ->
            async {
                uploadImage(urlInfo.url, imageBytes, "image/jpeg")
                    .map { urlInfo.saveId }
                    .getOrNull()
            }
        }

        val uploadedKeys = uploadJobs.awaitAll().filterNotNull()

        // 3. 업로드 완료 확인
        if (uploadedKeys.isNotEmpty()) {
            repository.checkReviewImageSave(saveResult.reviewId, uploadedKeys)
                .map { Unit }
        } else {
            Result.failure(Exception("이미지 업로드에 실패했습니다."))
        }
    }
}
