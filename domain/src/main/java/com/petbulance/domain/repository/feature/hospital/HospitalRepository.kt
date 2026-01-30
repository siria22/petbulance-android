package com.petbulance.domain.repository.feature.hospital

import com.petbulance.domain.model.common.PagingResult
import com.petbulance.domain.model.feature.hospital.hospital.Hospital
import com.petbulance.domain.model.feature.hospital.hospital.HospitalCard
import com.petbulance.domain.model.feature.hospital.hospital.HospitalDetail

interface HospitalRepository {
    suspend fun searchHospitals(
        q: String?,
        region: String?,
        lat: Double?,
        lng: Double?,
        bounds: String?,
        animal: String?,
        openNow: Boolean?,
        sortBy: String?,
        size: Int,
        cursorId: Long?,
        cursorDistance: Double?,
        cursorRating: Double?,
        cursorReviewCount: Long?
    ): Result<PagingResult<Hospital>>

    suspend fun getHospitalDetail(
        hospitalId: Long,
        userLat: Double,
        userLng: Double
    ): Result<HospitalDetail>

    suspend fun getHospitalCard(
        hospitalId: Long,
        userLat: Double,
        userLng: Double
    ): Result<HospitalCard>
}