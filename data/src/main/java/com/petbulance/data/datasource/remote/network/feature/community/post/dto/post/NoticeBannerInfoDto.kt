package com.petbulance.data.datasource.remote.network.feature.community.post.dto.post

import kotlinx.serialization.Serializable

@Serializable
data class NoticeBannerInfoDto(
    val noticeId: Long,
    val noticeStatus: String,
    val title: String,
    val content: String
)
