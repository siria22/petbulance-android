package com.example.data.repository.nonfeature.app

import com.example.data.datasource.remote.network.nonfeature.app.AppApi
import com.example.data.datasource.remote.network.nonfeature.app.dto.MetadataRequestDto
import com.example.data.datasource.remote.network.nonfeature.app.dto.MetadataResponseDto
import com.example.data.datasource.remote.network.nonfeature.app.dto.TestResponseDto
import com.example.data.datasource.remote.network.common.safeApiCall
import com.example.data.mapper.nonfeature.app.toDomain
import com.example.domain.model.nonfeature.app.HealthCheckResult
import com.example.domain.model.nonfeature.app.MetadataResponse
import com.example.domain.repository.nonfeature.app.AppInfoRepository
import javax.inject.Inject

class AppInfoRepositoryImpl @Inject constructor(
    private val api: AppApi
) : AppInfoRepository {

    override suspend fun checkHealth(): Result<HealthCheckResult> {
        return safeApiCall<TestResponseDto>(path = "/app/health") {
            api.healthCheck()
        }.map { dto ->
            HealthCheckResult(message = dto.message)
        }
    }

    override suspend fun checkError(): Result<Unit> {
        return safeApiCall<Unit>(path = "/app/error") {
            api.errorCheck()
        }.map { }
    } // Always throws error

    override suspend fun getVersion(): Result<String> {
        return safeApiCall<String>(path = "/app/version") {
            api.getVersion()
        }
    }

    override suspend fun getMetadata(
        region: String,
        species: String,
        communityCategory: String
    ): Result<MetadataResponse> {
        val requestDto = MetadataRequestDto(
            region = region,
            species = species,
            communityCategory = communityCategory
        )
        return safeApiCall<MetadataResponseDto>(path = "/app/metadata") {
            api.getMetadata(requestDto)
        }.map { it.toDomain() }
    }

}