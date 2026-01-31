package com.petbulance.presentation.screen.feature.review.search

import com.petbulance.domain.model.feature.hospital.recent.RecentSearchKeyword
import com.petbulance.domain.model.feature.hospital.review.HospitalReview
import com.petbulance.domain.model.type.ReviewSortType
import com.petbulance.presentation.screen.feature.search.main.views.search.HospitalSearchQueryUiModel

data class ReviewSearchData(
    val searchQueryModel: HospitalSearchQueryUiModel,
    val recentKeywords: List<RecentSearchKeyword>,
    val searchResults: List<HospitalReview>,
    val isSearchResultMode: Boolean,
    val isLoadingNextPage: Boolean,
    val selectedSort: ReviewSortType = ReviewSortType.LATEST,
    val isReceiptVerified: Boolean = false,
    val isPhotoReview: Boolean = false
)