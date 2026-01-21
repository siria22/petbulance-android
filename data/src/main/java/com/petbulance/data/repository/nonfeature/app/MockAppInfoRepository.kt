package com.petbulance.data.repository.nonfeature.app

import com.petbulance.domain.model.nonfeature.app.HealthCheckResult
import com.petbulance.domain.model.nonfeature.app.MetadataResponse
import com.petbulance.domain.model.nonfeature.app.PresignFileRequest
import com.petbulance.domain.model.nonfeature.app.PresignedUrl
import com.petbulance.domain.model.nonfeature.app.RegionsResponse
import com.petbulance.domain.repository.nonfeature.app.AppInfoRepository
import jakarta.inject.Inject

class MockAppInfoRepository @Inject constructor() : AppInfoRepository {
    override suspend fun checkHealth(): Result<HealthCheckResult> {
        return Result.success(
            HealthCheckResult(
                message = "Happy Energy ^O^",
                isHealthy = true
            )
        )
    }

    override suspend fun checkError(): Result<Unit> {
        return Result.failure(Exception("Forced Error"))
    }

    override suspend fun getVersion(): Result<String> {
        return Result.success("1.0.0")
    }

    override suspend fun getCurrentAppVersion(): Result<String> {
        return Result.success("1.0.0")
    }

    override suspend fun getMetadata(
        region: String,
        species: String,
        communityCategory: String
    ): Result<MetadataResponse> {
        return Result.success(mockMetadataResponse)
    }

    override suspend fun getPresignedUrl(files: List<PresignFileRequest>): Result<List<PresignedUrl>> {
        val mockPresignedUrlList = listOf(
            PresignedUrl(
                preSignedUrl = "mock presigned url",
                imageUrl = "mock image url"
            )
        )
        return Result.success(mockPresignedUrlList)
    }
}

private val mockMetadataResponse = MetadataResponse(
    region = RegionsResponse(
        regions1List = listOf(
            "서울", "경기", "인천", "부산", "대구", "대전", "광주", "제주"
        ),
        regions2List = listOf(
            "강남구", "서초구", "송파구", "마포구", "분당구", "일산동구", "해운대구"
        )
    ),

    // 2. 동물 종 (특수 동물 포함 다양하게 구성)
    species = listOf(
        "강아지",
        "고양이",
        "햄스터",
        "토끼",
        "앵무새",
        "고슴도치",
        "거북이",
        "도마뱀(파충류)"
    ),

    // 3. 커뮤니티 카테고리
    communityCategory = listOf(
        "자유게시판",
        "질문과 답변",
        "건강/질병 정보",
        "병원 방문 후기",
        "실종/제보",
        "중고장터"
    )
)