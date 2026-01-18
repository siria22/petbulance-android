package com.example.domain.usecase.feature.hospital.review

import com.example.domain.model.feature.hospital.recent.RecentSearchKeyword
import com.example.domain.repository.feature.hospital.SearchRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentSearchKeywordsUseCase @Inject constructor(
    private val repository: SearchRepository
) {
    operator fun invoke(): Flow<List<RecentSearchKeyword>> {
        return repository.getSearchHistoryStream()
    }
}