package com.petbulance.domain.usecase.feature.hospital.hospital

import com.petbulance.domain.model.feature.hospital.hospital.HospitalCard
import com.petbulance.domain.repository.feature.hospital.HospitalRepository
import javax.inject.Inject

class GetHospitalCardUseCase @Inject constructor(
    private val repository: HospitalRepository
) {
    suspend operator fun invoke(
        hospitalId: Long,
        userLat: Double,
        userLng: Double
    ): HospitalCard {
        return repository.getHospitalCard(
            hospitalId = hospitalId,
            userLat = userLat,
            userLng = userLng
        ).getOrThrow()
    }
}