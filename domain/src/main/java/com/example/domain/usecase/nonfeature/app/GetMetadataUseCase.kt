package com.example.domain.usecase.nonfeature.app

import com.example.domain.model.nonfeature.app.MetadataResponse
import com.example.domain.repository.nonfeature.app.AppInfoRepository
import javax.inject.Inject

class GetMetadataUseCase @Inject constructor(
    private val repository: AppInfoRepository
) {
    suspend operator fun invoke(
        region: String,
        species: String,
        communityCategory: String
    ): MetadataResponse {
        return repository.getMetadata(region, species, communityCategory).getOrThrow()
    }
}