package com.example.domain.model.nonfeature.app

data class MetadataResponse(
    val region: RegionsResponse = RegionsResponse(),
    val species: List<String> = emptyList(),
    val communityCategory: List<String> = emptyList()
)

data class RegionsResponse(
    val regions1List: List<String> = emptyList(),
    val regions2List: List<String> = emptyList()
)