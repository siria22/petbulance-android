package com.example.domain.usecase.feature.hospital.hospital

import com.example.domain.model.feature.hospital.hospital.HospitalDetail
import com.example.domain.repository.feature.hospital.HospitalRepository
import javax.inject.Inject

class GetHospitalDetailUseCase @Inject constructor(
    private val repository: HospitalRepository
) {
    suspend operator fun invoke(hospitalId: Long): HospitalDetail {
        return repository.getHospitalDetail(hospitalId).getOrThrow()
    }
}