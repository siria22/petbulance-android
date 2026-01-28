package com.petbulance.domain.usecase.feature.hospital.review

import com.petbulance.domain.model.feature.hospital.review.HospitalInfoForReview
import com.petbulance.domain.repository.feature.hospital.ReviewRepository
import javax.inject.Inject

class FindHospitalIdByNameUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    suspend operator fun invoke(hospitalName: String): Result<List<HospitalInfoForReview>> = runCatching {
        return repository.findHospital(hospitalName)
    }
}