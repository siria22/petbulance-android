package com.example.domain.usecase.feature.hospital.review

import com.example.domain.model.feature.home.HomeScreenReview
import com.example.domain.model.feature.hospital.review.HospitalReview
import com.example.domain.repository.feature.hospital.ReviewRepository
import javax.inject.Inject

class GetRecentReviewsUseCase @Inject constructor(
    private val repository: ReviewRepository
) {
    suspend operator fun invoke(): List<HomeScreenReview> {
        //TODO : Fetch recent reviews by some criteria
        return listOf(HomeScreenReview.stub())
    }
}