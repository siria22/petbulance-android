package com.petbulance.domain.usecase.feature.hospital.recent

import com.petbulance.domain.model.feature.hospital.recent.ViewedHospital
import com.petbulance.domain.repository.feature.hospital.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetViewedHospitalsUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    operator fun invoke(): Flow<List<ViewedHospital>> {
        return repository.getViewedHospitalsStream()
    }
}