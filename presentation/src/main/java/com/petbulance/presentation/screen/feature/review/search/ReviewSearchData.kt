package com.petbulance.presentation.screen.feature.review.search

import com.petbulance.domain.model.feature.hospital.recent.RecentSearchKeyword
import com.petbulance.domain.model.feature.hospital.review.HospitalReview
import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.domain.model.type.Region
import com.petbulance.domain.model.type.ReviewSortType

data class ReviewSearchData(
    val query: String,
    val recentKeywords: List<RecentSearchKeyword>,
    val searchResults: List<HospitalReview>,
    val isSearchResultMode: Boolean,
    val isLoadingNextPage: Boolean,

    val selectedRegion: Region? = null,
    val selectedDistrict: String? = null,
    val selectedAnimalType: AnimalCategory? = null,
    val selectedSort: ReviewSortType = ReviewSortType.LATEST,
    val isReceiptVerified: Boolean = false,
    val isPhotoReview: Boolean = false
)