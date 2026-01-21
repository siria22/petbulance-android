package com.petbulance.domain.model.nonfeature.app

data class PresignFileRequest(
    val filename: String,
    val contentType: String
)

data class PresignedUrl(
    val preSignedUrl: String,
    val imageUrl: String
)