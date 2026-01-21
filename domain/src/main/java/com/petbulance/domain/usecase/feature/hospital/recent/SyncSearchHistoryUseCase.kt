package com.petbulance.domain.usecase.feature.hospital.recent

import com.petbulance.domain.repository.feature.hospital.SearchRepository
import javax.inject.Inject

class SyncSearchHistoryUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke() {
        repository.syncSearchHistory()
    }
}