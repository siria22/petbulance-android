package com.petbulance.domain.usecase.feature.community.search

import com.petbulance.domain.model.feature.hospital.recent.RecentSearchKeyword
import com.petbulance.domain.repository.feature.hospital.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentSearchKeywordsUseCase @Inject constructor(
    private val searchRepository: SearchRepository
) {
    operator fun invoke(): Flow<List<RecentSearchKeyword>> {
        return searchRepository.getSearchHistoryStream()
    }
}
