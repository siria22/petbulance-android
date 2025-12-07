package com.example.data.datasource.remote.network.feature.user.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProfileImageUpdateReqDto(
    val filename: String,
    val contentType: String // ex: "image/jpeg"
)