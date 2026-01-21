package com.petbulance.data.datasource.remote.network.nonfeature.app.dto

import kotlinx.serialization.Serializable

@Serializable
data class MetadataRequestDto(
    val region: String,
    val species: String,
    val communityCategory: String
)