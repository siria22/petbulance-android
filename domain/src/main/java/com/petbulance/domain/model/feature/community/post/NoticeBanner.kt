package com.petbulance.domain.model.feature.community.post

data class NoticeBanner(
    val noticeId: Long,
    val noticeStatus: String,
    val title: String,
    val content: String
)
