package com.petbulance.data.datasource.remote.network.feature.user.authority.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthorityResDto(
    val locationService: Boolean,
    val marketing: Boolean,
    val camera: Boolean
)
