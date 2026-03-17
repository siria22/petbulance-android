package com.petbulance.domain.usecase.feature.community.search

import com.petbulance.domain.repository.feature.hospital.SearchRepository
import javax.inject.Inject

class DeleteRecentSearchKeywordUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(keyword: String) {
        searchRepository.deleteSearchKeyword(keyword)
    }
}
