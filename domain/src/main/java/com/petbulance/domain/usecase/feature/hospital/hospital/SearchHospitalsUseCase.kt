package com.petbulance.domain.usecase.feature.hospital.hospital

import com.petbulance.domain.model.common.PagingResult
import com.petbulance.domain.model.feature.hospital.hospital.Hospital
import com.petbulance.domain.model.feature.hospital.hospital.MapBounds
import com.petbulance.domain.repository.feature.hospital.HospitalRepository
import javax.inject.Inject

class SearchHospitalsUseCase @Inject constructor(
    private val repository: HospitalRepository
) {
    suspend operator fun invoke(
        q: String?,
        region: String?,
        lat: Double?,
        lng: Double?,
        bounds: MapBounds?,
        animal: String?,
        openNow: Boolean?,
        sortBy: String?,
        size: Int,
        cursorId: Long?,
        cursorDistance: Double?,
        cursorRating: Double?,
        cursorReviewCount: Long?
    ): PagingResult<Hospital> {
        return repository.searchHospitals(
            q = q,
            region = region,
            lat = lat,
            lng = lng,
            bounds = bounds?.let { "${it.minLat},${it.minLng},${it.maxLat},${it.maxLng}" },
            animal = animal,
            openNow = openNow,
            sortBy = sortBy,
            size = size,
            cursorId = cursorId,
            cursorDistance = cursorDistance,
            cursorRating = cursorRating,
            cursorReviewCount = cursorReviewCount
        ).getOrThrow()
    }
}