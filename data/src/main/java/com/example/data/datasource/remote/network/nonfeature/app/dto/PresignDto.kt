package com.example.data.datasource.remote.network.nonfeature.app.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetPresignReqDto(
    val files: List<NoticeFileReqDto>
)

@Serializable
data class NoticeFileReqDto(
    val filename: String,
    val contentType: String
)

@Serializable
data class GetPresignResDto(
    val uploadedFiles: List<UrlInfo>
)

@Serializable
data class UrlInfo(
    val preSignedUrl: String,
    val imageUrl: String
)