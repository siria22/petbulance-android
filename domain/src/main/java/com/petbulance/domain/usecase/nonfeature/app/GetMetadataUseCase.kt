package com.petbulance.domain.usecase.nonfeature.app

import com.petbulance.domain.model.nonfeature.app.MetadataResponse
import com.petbulance.domain.repository.nonfeature.app.AppInfoRepository
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