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
        page: Int,
        size: Int
    ): PagingResult<Hospital> {
        return repository.searchHospitals(
            q = null,
            region = null,
            lat = null,
            lng = null,
            bounds = null,
            animal = null,
            openNow = null,
            page = page,
            size = 10 // Project REQ
        ).getOrThrow()
    }
}