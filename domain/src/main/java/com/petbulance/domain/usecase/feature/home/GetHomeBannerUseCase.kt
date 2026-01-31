package com.petbulance.domain.usecase.feature.home

import com.petbulance.domain.model.feature.home.HomeBanner
import com.petbulance.domain.repository.feature.home.BannerRepository
import javax.inject.Inject

class GetHomeBannersUseCase @Inject constructor(
    private val repository: BannerRepository
) {
    suspend operator fun invoke(): Result<List<HomeBanner>> {
        return repository.getHomeBanners()
    }
}