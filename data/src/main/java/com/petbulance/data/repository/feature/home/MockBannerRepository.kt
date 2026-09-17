package com.petbulance.data.repository.feature.home

import com.petbulance.domain.model.feature.home.HomeBanner
import com.petbulance.domain.repository.feature.home.BannerRepository
import javax.inject.Inject

/** 서버 이미지가 없어 배너를 비운다. 홈 화면은 배너 목록이 비면 슬라이더를 그리지 않는다. */
class MockBannerRepository @Inject constructor() : BannerRepository {
    override suspend fun getHomeBanners(): Result<List<HomeBanner>> = Result.success(emptyList())
}
