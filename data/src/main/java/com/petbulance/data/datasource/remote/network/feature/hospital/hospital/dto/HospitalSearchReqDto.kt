package com.petbulance.data.datasource.remote.network.feature.hospital.hospital.dto

import kotlinx.serialization.Serializable

@Serializable
data class HospitalSearchReqDto(
    val q: String? = null,
    val region: String? = null,
    val bounds: String? = null,
    val animal: String? = null,
    val openNow: Boolean? = null,
    val sortBy: String? = null,
    val size: Int? = null,
    val cursorId: Long? = null,
    val cursorDistance: Double? = null,
    val cursorRating: Double? = null,
    val cursorReviewCount: Long? = null
)