package com.example.presentation.screen.feature.home

import com.example.domain.model.feature.community.post.PostDetail
import com.example.domain.model.feature.hospital.review.HospitalReview

data class HomeData(
    val recentReviews: List<HospitalReview>,
    val hotArticles: List<PostDetail>?
) {
    companion object {
        val stub = HomeData(
            recentReviews = listOf(HospitalReview.stub),
            hotArticles = listOf(PostDetail.stub)
        )
    }
}