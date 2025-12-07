package com.example.data.datasource.remote.network.feature.community.board.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BoardResDto(
    @SerialName("boardId")
    val boardId: Long,
    val nameKr: String,
    val nameEn: String,
    val description: String
)