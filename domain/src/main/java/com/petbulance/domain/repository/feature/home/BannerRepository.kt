package com.petbulance.domain.repository.feature.home

import com.petbulance.domain.model.feature.home.HomeBanner

interface BannerRepository {
    suspend fun getHomeBanners(): Result<List<HomeBanner>>
}