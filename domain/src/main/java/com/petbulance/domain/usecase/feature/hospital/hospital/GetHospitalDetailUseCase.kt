package com.petbulance.domain.usecase.feature.hospital.hospital

import com.petbulance.domain.model.feature.hospital.hospital.HospitalDetail
import com.petbulance.domain.repository.feature.hospital.HospitalRepository
import javax.inject.Inject

class GetHospitalDetailUseCase @Inject constructor(
    private val repository: HospitalRepository
) {
    suspend operator fun invoke(
        hospitalId: Long,
        userLat: Double,
        userLng: Double
    ): HospitalDetail {
        return repository.getHospitalDetail(hospitalId, userLat, userLng).getOrThrow()
    }
}