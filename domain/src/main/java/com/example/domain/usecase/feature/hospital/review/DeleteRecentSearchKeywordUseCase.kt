package com.example.domain.usecase.feature.hospital.review

import com.example.domain.repository.feature.hospital.SearchRepository
import javax.inject.Inject

class DeleteRecentSearchKeywordUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    suspend operator fun invoke(keyword: String) {
        repository.deleteSearchKeyword(keyword)
    }
}