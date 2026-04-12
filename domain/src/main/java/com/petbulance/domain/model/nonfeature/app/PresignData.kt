package com.petbulance.domain.model.nonfeature.app

data class PresignFileRequest(
    val filename: String,
    val contentType: String,
    val usage: String = "COMMENT"
)

data class PresignedUrl(
    val preSignedUrl: String,
    val imageUrl: String
)