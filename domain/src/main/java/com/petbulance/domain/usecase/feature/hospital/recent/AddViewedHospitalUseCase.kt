package com.petbulance.domain.usecase.feature.hospital.recent

import com.petbulance.domain.repository.feature.hospital.SearchRepository
import javax.inject.Inject

class AddViewedHospitalUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(hospitalId: Long, hospitalName: String) {
        repository.addViewedHospital(hospitalId, hospitalName)
    }
}