package com.petbulance.data.datasource.remote.network.feature.hospital.review.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReceiptAnalysisResDto(
    val hospitalId: Long,
    val hospitalName: String,
    val visitDateTime: String,
    val price: Long,
    val items: List<ReceiptItemDto>
)