package com.example.domain.model.feature.home

data class HomeBanner(
    val bannerId: Long,
    val imageUrl: String,
    val noticeId: Long,
    val startDate: String,
    val endDate: String
)