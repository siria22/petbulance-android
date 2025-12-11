package com.example.domain.usecase.feature.hospital.recent

import com.example.domain.repository.feature.hospital.SearchRepository
import javax.inject.Inject

class AddSearchKeywordUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(keyword: String) {
        if (keyword.isBlank()) return
        repository.addSearchKeyword(keyword)
    }
}