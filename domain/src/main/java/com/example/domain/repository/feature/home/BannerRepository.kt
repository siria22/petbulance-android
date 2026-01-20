package com.example.domain.repository.feature.home

import com.example.domain.model.feature.home.HomeBanner

interface BannerRepository {
    suspend fun getHomeBanners(): Result<List<HomeBanner>>
}