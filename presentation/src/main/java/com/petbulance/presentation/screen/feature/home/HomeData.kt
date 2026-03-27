package com.petbulance.presentation.screen.feature.home

import com.petbulance.domain.model.feature.community.post.PostSummary
import com.petbulance.domain.model.feature.home.HomeBanner
import com.petbulance.domain.model.feature.home.HomeScreenReview

data class HomeData(
    val recentReviews: List<HomeScreenReview>,
    val hotArticles: List<PostSummary>,
    val homeBanners: List<HomeBanner>
) {
    companion object {
        val stub = HomeData(
            recentReviews = listOf(HomeScreenReview.stub()),
            hotArticles = listOf(
                PostSummary(
                    id = 1,
                    type = "SMALLMAMMALS",
                    topic = "DAILY",
                    title = "기니피그 자랑",
                    content = "",
                    thumbnailUrl = null,
                    imageCount = 0,
                    viewCount = 50,
                    commentCount = 1,
                    likeCount = 5,
                    createdAt = "2026.03.16",
                    isLiked = false
                ),
                PostSummary(
                    id = 2,
                    type = "FISH",
                    topic = "SUPPLIES",
                    title = "베타 어항 바닥재 추천부탁드립니다",
                    content = "",
                    thumbnailUrl = null,
                    imageCount = 0,
                    viewCount = 30,
                    commentCount = 0,
                    likeCount = 3,
                    createdAt = "2026.03.20",
                    isLiked = false
                )
            ),
            homeBanners = emptyList()
        )
    }
}
