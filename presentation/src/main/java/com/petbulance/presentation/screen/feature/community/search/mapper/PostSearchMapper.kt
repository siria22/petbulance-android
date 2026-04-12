package com.petbulance.presentation.screen.feature.community.search.mapper

import com.petbulance.domain.model.feature.community.post.PostSearchSummary
import com.petbulance.domain.model.feature.community.post.PostSummary

/**
 * PostSearchSummary를 PostSummary로 변환
 *
 * PostSearchSummary는 이미 type, topic을 가지고 있음
 */
fun PostSearchSummary.toPostSummary(): PostSummary {
    return PostSummary(
        id = id,
        type = type,
        topic = topic,
        title = title,
        content = content,
        thumbnailUrl = thumbnailUrl,
        imageCount = imageCount,
        viewCount = viewCount,
        commentCount = commentCount,
        likeCount = likeCount,
        createdAt = createdAt,
        isLiked = isLiked
    )
}

/**
 * PostSearchSummary 리스트를 PostSummary 리스트로 변환
 */
fun List<PostSearchSummary>.toPostSummaryList(): List<PostSummary> {
    return this.map { it.toPostSummary() }
}
