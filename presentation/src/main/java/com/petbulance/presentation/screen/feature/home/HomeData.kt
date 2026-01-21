package com.petbulance.presentation.screen.feature.home

import com.petbulance.domain.model.feature.community.post.PostDetail
import com.petbulance.domain.model.feature.home.HomeScreenReview

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