package com.petbulance.domain.usecase.feature.community.search

import com.petbulance.domain.repository.feature.hospital.SearchRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class DeleteAllRecentSearchKeywordsUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    suspend operator fun invoke(): List<String> {
        val keywords = searchRepository.getSearchHistoryStream().first()
        keywords.forEach { keyword ->
            searchRepository.deleteSearchKeyword(keyword.keyword)
        }
        return keywords.map { it.keyword }
    }
}
