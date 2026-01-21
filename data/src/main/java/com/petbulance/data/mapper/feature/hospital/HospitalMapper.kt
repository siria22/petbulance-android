package com.petbulance.data.mapper.feature.hospital

import com.petbulance.data.datasource.remote.network.feature.hospital.hospital.dto.HospitalCardResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.hospital.dto.HospitalDetailResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.hospital.dto.HospitalsResDto
import com.petbulance.data.datasource.remote.network.feature.hospital.hospital.dto.OpenHourResDto
import com.petbulance.domain.model.feature.hospital.hospital.Hospital
import com.petbulance.domain.model.feature.hospital.hospital.HospitalCard
import com.petbulance.domain.model.feature.hospital.hospital.HospitalDetail
import com.petbulance.domain.model.feature.hospital.hospital.OpenHour

fun HospitalsResDto.toDomain(): Hospital {
    return Hospital(
        hospitalId = hospitalId,
        name = name,
        lat = lat,
        lng = lng,
        distanceMeters = distanceMeters,
        phone = phone,
        types = types,
        isOpenNow = isOpenNow,
        openHours = openHours,
        thumbnailUrl = thumbnailUrl,
        rating = rating,
        reviewCount = reviewCount
    )
}

fun HospitalDetailResDto.toDomain(): HospitalDetail {
    return HospitalDetail(
        hospitalId = hospitalId,
        name = name,
        address = address,
        lat = lat,
        lng = lng,
        phone = phone,
        acceptedAnimals = acceptedAnimals,
        openHours = openHours.map { it.toDomain() },
        notes = notes,
        openNow = openNow,
        description = description,
        rating = rating,             // 매핑 추가
        reviewCount = reviewCount,   // 매핑 추가
        thumbnailUrl = thumbnailUrl  // 매핑 추가
    )
}

fun OpenHourResDto.toDomain(): OpenHour {
    return OpenHour(
        day = day,
        hours = hours
    )
}

fun HospitalCardResDto.toDomain(): HospitalCard {
    return HospitalCard(
        hospitalId = hospitalId,
        name = name,
        lat = lat,
        lng = lng,
        distanceMeters = distanceMeters,
        phone = phone,
        types = types,
        isOpenNow = isOpenNow,
        nextOpenHours = nextOpenHours,
        thumbnailUrl = thumbnailUrl,
        rating = rating,
        reviewCount = reviewCount
    )
}

