package com.petbulance.data.repository.feature.home

import com.petbulance.domain.model.feature.home.HomeBanner
import com.petbulance.domain.repository.feature.home.BannerRepository
import javax.inject.Inject

class MockBannerRepository @Inject constructor() : BannerRepository {
    override suspend fun getHomeBanners(): Result<List<HomeBanner>> =
        Result.success(
            listOf(
                HomeBanner(
                    1L, "https://example.com/banner1.png", 1,
                    startDate = "2025-01-01",
                    endDate = "2025-01-02",
                )
            )
        )
}