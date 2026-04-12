package com.petbulance.data.datasource.remote.network.feature.support.notice.dto

import kotlinx.serialization.Serializable

@Serializable
data class NoticeButtonDto(
    val buttonId: Long,
    val text: String,
    val position: String,
    val link: String,
    val target: String
)
