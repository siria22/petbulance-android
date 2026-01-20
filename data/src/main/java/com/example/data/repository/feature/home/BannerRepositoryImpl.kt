package com.example.data.repository.feature.home

import com.example.data.datasource.remote.network.common.safeApiCall
import com.example.data.datasource.remote.network.feature.home.BannerApi
import com.example.data.datasource.remote.network.feature.home.dto.HomeBannerListResDto
import com.example.domain.model.feature.home.HomeBanner
import com.example.domain.repository.feature.home.BannerRepository
import javax.inject.Inject

class BannerRepositoryImpl @Inject constructor(
    private val api: BannerApi
) : BannerRepository {

    override suspend fun getHomeBanners(): Result<List<HomeBanner>> {
        return safeApiCall<List<HomeBannerListResDto>>("banners/home") {
            api.getHomeBanners()
        }.map { list ->
            list.map { dto ->
                HomeBanner(
                    bannerId = dto.bannerId,
                    imageUrl = dto.imageUrl,
                    noticeId = dto.noticeId,
                    startDate = dto.startDate,
                    endDate = dto.endDate
                )
            }
        }
    }
}