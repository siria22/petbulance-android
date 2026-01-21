package com.petbulance.data.datasource.remote.network.feature.home.dto

import kotlinx.serialization.Serializable

@Serializable
data class HomeBannerListResDto(
    val bannerId: Long,
    val startDate: String,
    val endDate: String,
    val noticeId: Long,
    val imageUrl: String
)