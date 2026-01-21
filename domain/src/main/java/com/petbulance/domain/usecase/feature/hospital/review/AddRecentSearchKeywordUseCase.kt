package com.petbulance.domain.usecase.feature.hospital.review

import com.petbulance.domain.repository.feature.hospital.SearchRepository
import javax.inject.Inject

class AddRecentSearchKeywordUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(keyword: String) {
        if (keyword.isBlank()) return
        repository.addSearchKeyword(keyword)
    }
}