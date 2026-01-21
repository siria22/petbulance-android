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
        page: Int,
        size: Int
    ): Result<PagingResult<Hospital>>

    suspend fun getHospitalDetail(hospitalId: Long): Result<HospitalDetail>

    suspend fun getHospitalCard(
        hospitalId: Long,
        userLat: Double,
        userLng: Double
    ): Result<HospitalCard>
}