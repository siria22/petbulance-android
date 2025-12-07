package com.example.data.datasource.remote.network.feature.community.recent.dto

import kotlinx.serialization.Serializable

@Serializable
data class DeleteKeywordResDto(
    val success: Boolean,
    val message: String
)