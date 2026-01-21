package com.example.presentation.screen.feature.home

import com.example.domain.model.feature.community.post.PostDetail
import com.example.domain.model.feature.home.HomeScreenReview
import com.example.domain.model.feature.hospital.review.HospitalReview

data class HomeData(
    val recentReviews: List<HomeScreenReview>,
    val hotArticles: List<PostDetail>?
) {
    companion object {
        val stub = HomeData(
            recentReviews = listOf(HomeScreenReview.stub()),
            hotArticles = listOf(PostDetail.stub)
        )
    }
}