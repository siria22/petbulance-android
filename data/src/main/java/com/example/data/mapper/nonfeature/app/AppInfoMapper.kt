package com.example.data.mapper.nonfeature.app

import com.example.data.datasource.remote.network.nonfeature.app.dto.MetadataResponseDto
import com.example.domain.model.nonfeature.app.MetadataResponse
import com.example.domain.model.nonfeature.app.RegionsResponse

fun MetadataResponseDto.toDomain() = MetadataResponse(
    region = RegionsResponse(
        regions1List = region.regions1List,
        regions2List = region.regions2List
    ),
    species = species,
    communityCategory = communityCategory
)