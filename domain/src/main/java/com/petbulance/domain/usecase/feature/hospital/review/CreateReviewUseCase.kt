package com.petbulance.domain.usecase.feature.hospital.review

import com.petbulance.domain.model.feature.hospital.review.SaveReviewParam
import com.petbulance.domain.model.feature.hospital.review.SaveReviewResult
import com.petbulance.domain.repository.feature.hospital.ReviewRepository
import com.petbulance.domain.usecase.nonfeature.app.UploadImageUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class CreateReviewUseCase @Inject constructor(
    private val repository: ReviewRepository,
    private val uploadImage: UploadImageUseCase
) {
    /**
     * 리뷰 등록 및 이미지 업로드 프로세스
     * 1. 리뷰 메타데이터 저장 (Presigned URL 획득)
     * 2. 획득한 URL로 이미지 바이너리(ByteArray) 직접 업로드
     * 3. 업로드 완료 확인 요청
     *
     * @param param 리뷰 기본 정보 (이미지 제외)
     * @param images 이미지 바이너리 리스트. ViewModel에서 Uri를 통해 변환되어야 함.
     */
    suspend operator fun invoke(
        param: SaveReviewParam,
        images: List<ByteArray>
    ): Result<SaveReviewResult> = coroutineScope {
        // 1. 리뷰 정보 저장 요청 (재시도 1회)
        val saveResult = repository.saveReview(param).getOrElse { firstError ->
            // 네트워크 재시도 로직
            repository.saveReview(param).getOrElse { secondError ->
                return@coroutineScope Result.failure(secondError)
            }
        }

        val uploadUrls = saveResult.uploadUrls

        // 업로드할 이미지가 없거나 URL을 받지 못한 경우 종료
        if (images.isEmpty() || uploadUrls.isEmpty()) {
            return@coroutineScope Result.success(saveResult)
        }

        // 2. Presigned URL을 통한 이미지 업로드 실행 (재시도 포함)
        val uploadJobs = uploadUrls.zip(images).map { (urlInfo, imageBytes) ->
            async {
                // 첫 번째 시도
                uploadImage(urlInfo.url, imageBytes, "image/jpeg")
                    .map { urlInfo.saveId }
                    .getOrElse {
                        // 실패 시 재시도 1회
                        uploadImage(urlInfo.url, imageBytes, "image/jpeg")
                            .map { urlInfo.saveId }
                            .getOrNull()
                    }
            }
        }

        val uploadedKeys = uploadJobs.awaitAll().filterNotNull()

        // 3. 서버에 이미지 업로드 완료 상태 전송
        if (uploadedKeys.isNotEmpty()) {
            // 부분 성공: 성공한 이미지만 전송
            repository.checkReviewImageSave(saveResult.reviewId, uploadedKeys)
                .map { saveResult }
        } else {
            // 전체 실패
            Result.failure(Exception("이미지 업로드에 실패했습니다."))
        }
    }
}
