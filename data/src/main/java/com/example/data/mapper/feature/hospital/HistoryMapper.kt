package com.example.data.mapper.feature.hospital

import com.example.data.datasource.remote.network.feature.hospital.history.dto.RecentHospitalResDto
import com.example.data.datasource.remote.network.feature.hospital.history.dto.RecentHospitalSaveResDto
import com.example.data.datasource.remote.network.feature.hospital.history.dto.ViewedHospitalDto
import com.example.domain.model.feature.hospital.recent.RecentSearchKeyword
import com.example.domain.model.feature.hospital.recent.ViewedHospital

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

