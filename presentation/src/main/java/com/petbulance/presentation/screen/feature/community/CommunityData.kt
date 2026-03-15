package com.petbulance.presentation.screen.feature.community

import com.petbulance.domain.model.feature.community.post.NoticeBanner
import com.petbulance.domain.model.feature.community.post.PostSummary

data class CommunityData(
    val noticeBanner: NoticeBanner?,
    val posts: List<PostSummary>,
    val hasNext: Boolean,
    val currentType: String?,
    val currentTopic: String?,
    val currentSort: String
) {
    companion object {
        val empty = CommunityData(
            noticeBanner = null,
            posts = emptyList(),
            hasNext = false,
            currentType = null,
            currentTopic = null,
            currentSort = "latest"
        )

        fun stub() = CommunityData(
            noticeBanner = null,
            posts = emptyList(),
            hasNext = false,
            currentType = null,
            currentTopic = null,
            currentSort = "latest"
        )
    }
}