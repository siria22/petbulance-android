package com.petbulance.data.datasource.remote.network.nonfeature.app.dto

import kotlinx.serialization.Serializable

@Serializable
data class MetadataResponseDto(
    val region: RegionsResponseDto = RegionsResponseDto(),
    val species: List<String> = emptyList(),
    val communityCategory: List<String> = emptyList()
)

@Serializable
data class RegionsResponseDto(
    val regions1List: List<String> = emptyList(),
    val regions2List: List<String> = emptyList()
)