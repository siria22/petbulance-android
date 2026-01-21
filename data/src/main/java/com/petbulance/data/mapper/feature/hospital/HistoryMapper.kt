package com.petbulance.data.mapper.feature.hospital

import com.petbulance.data.datasource.remote.network.feature.hospital.history.dto.RecentHospitalResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.history.dto.RecentHospitalSaveResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.history.dto.ViewedHospitalDto
import com.petbulance.domain.model.feature.hospital.recent.RecentSearchKeyword
import com.petbulance.domain.model.feature.hospital.recent.ViewedHospital

fun RecentHospitalResDto.toDomain() = RecentSearchKeyword(
    id = keywordId,
    keyword = keyword,
    date = createdAt
)

fun RecentHospitalSaveResDto.toDomain() = RecentSearchKeyword(
    id = keywordId,
    keyword = keyword,
    date = createdAt
)

fun ViewedHospitalDto.toDomain() = ViewedHospital(
    hospitalId = hospitalId,
    hospitalName = name,
    viewedAt = viewedAt
)

