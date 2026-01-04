package com.example.data.datasource.remote.network.feature.user.terms.dto

import kotlinx.serialization.Serializable

@Serializable
data class TermsConsentRequestDto(
    val termsId: List<Long>
)