package com.petbulance.data.datasource.remote.network.feature.hospital.hospital.dto

import kotlinx.serialization.Serializable

@Serializable
data class HospitalSearchReqDto(
    val q: String? = null,
    val region: String? = null,
    val lat: Double? = null,
    val lng: Double? = null,
    val bounds: String? = null,
    val animal: String? = null,
    val openNow: Boolean? = null
)