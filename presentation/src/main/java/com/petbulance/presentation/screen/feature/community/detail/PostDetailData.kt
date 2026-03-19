package com.petbulance.presentation.screen.feature.community.detail

import com.petbulance.domain.model.feature.community.post.Comment

data class PostDetailData(
    val postId: Long,
    val boardName: String,
    val category: String,
    val writerNickname: String,
    val profileUrl: String?,
    val createdAt: String,
    val title: String,
    val content: String,
    val images: List<String>,
    val likeCount: Int,
    val commentCount: Int,
    val viewCount: Int,
    val isLiked: Boolean,
    val isMine: Boolean,
    val comments: List<Comment> = emptyList(),
    val hasMoreComments: Boolean = false,
    val totalCommentCount: Long = 0L,
    val commentImageUri: android.net.Uri? = null,
    val commentImageUrl: String? = null
) {
    companion object {
        val empty = PostDetailData(
            postId = 0L,
            boardName = "",
            category = "",
            writerNickname = "",
            profileUrl = null,
            createdAt = "",
            title = "",
            content = "",
            images = emptyList(),
            likeCount = 0,
            commentCount = 0,
            viewCount = 0,
            isLiked = false,
            isMine = false,
            comments = emptyList(),
            hasMoreComments = false,
            totalCommentCount = 0L,
            commentImageUri = null,
            commentImageUrl = null
        )

        fun stub(isMine: Boolean = false) = PostDetailData(
            postId = 1L,
            boardName = "소형포유류",
            category = "일상/자랑",
            writerNickname = "햄스터집사",
            profileUrl = "https://example.com/profile.jpg",
            createdAt = "1시간 전",
            title = "우리 햄스터 너무 귀엽지 않나요?",
            content = "오늘 햄스터가 처음으로 손에서 밥을 먹었어요! 너무 귀여워서 영상 찍었는데 공유하고 싶어서 올립니다. 다들 반려동물 키우시면서 이런 순간들 있으시죠?",
            images = listOf(
                "https://example.com/hamster1.jpg",
                "https://example.com/hamster2.jpg"
            ),
            likeCount = 24,
            commentCount = 8,
            viewCount = 132,
            isLiked = false,
            isMine = isMine,
            comments = emptyList(),
            hasMoreComments = false,
            totalCommentCount = 0L,
            commentImageUri = null,
            commentImageUrl = null
        )
    }
}
