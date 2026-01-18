package com.example.presentation.screen.feature.review

import com.example.domain.model.feature.hospital.review.HospitalReview
import com.example.domain.model.type.AnimalCategory
import com.example.domain.model.type.Region
import com.example.domain.model.type.ReviewSortType

data class ReviewData(
    val reviews: List<HospitalReview>,
    val selectedRegion: Region?,
    val selectedDistrict: String?,
    val selectedAnimalType: AnimalCategory?,
    val selectedSort: ReviewSortType,
    val isReceiptVerified: Boolean,
    val isPhotoReview: Boolean,
    val isLoadingNextPage: Boolean
)