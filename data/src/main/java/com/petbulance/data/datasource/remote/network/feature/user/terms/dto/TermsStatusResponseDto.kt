package com.petbulance.data.datasource.remote.network.feature.user.terms.dto

import kotlinx.serialization.Serializable

@Serializable
data class TermsStatusResponseDto(
    val service: String,   // AGREE, DISAGREE, EXPIRED
    val privacy: String,
    val location: String,
    val marketing: String
)

