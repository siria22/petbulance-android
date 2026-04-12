package com.petbulance.presentation.screen.feature.community.detail

import com.petbulance.domain.model.feature.community.post.Comment
import com.petbulance.domain.model.feature.community.post.WriterInfo

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
    val commentImageUrl: String? = null,
    val editingCommentId: Long? = null,
    val editingCommentContent: String = "",
    val editingCommentImageUrl: String? = null,
    val editingCommentIsSecret: Boolean = false
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
            commentImageUrl = null,
            editingCommentId = null,
            editingCommentContent = "",
            editingCommentImageUrl = null,
            editingCommentIsSecret = false
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
            comments = listOf(
                Comment(
                    isRoot = true,
                    commentId = 1L,
                    parentId = 0L,
                    writerInfo = WriterInfo("햄스러버", null),
                    mentionUserNickname = null,
                    content = "우와 햄스터 정말 귀여워요! 저희 집 햄스터도 아직 손에서 안 먹는데 ㅠㅠ 팁 좀 알려주세요!",
                    isSecret = false,
                    isCommentFromPostAuthor = false,
                    isCommentAuthor = false,
                    deleted = false,
                    hidden = false,
                    imageUrl = null,
                    visibleToUser = true,
                    createdAt = "30분 전"
                ),
                Comment(
                    isRoot = false,
                    commentId = 2L,
                    parentId = 1L,
                    writerInfo = WriterInfo("햄스터집사", null),
                    mentionUserNickname = "햄스러버",
                    content = "먼저 햄스터가 손에 익숙해지게 하는 게 중요해요! 손에 간식을 올려놓고 기다리다 보면 먹게 될거예요.",
                    isSecret = false,
                    isCommentFromPostAuthor = true,
                    isCommentAuthor = true,
                    deleted = false,
                    hidden = false,
                    imageUrl = null,
                    visibleToUser = true,
                    createdAt = "25분 전"
                ),
                Comment(
                    isRoot = true,
                    commentId = 3L,
                    parentId = 0L,
                    writerInfo = WriterInfo("햄스터고수", null),
                    mentionUserNickname = null,
                    content = "저희 집 햄스터도 처음에는 무서워했는데, 일주일 정도 꾸준히 손으로 간식 주니까 이제는 손만 보면 달려와요!",
                    isSecret = false,
                    isCommentFromPostAuthor = false,
                    isCommentAuthor = false,
                    deleted = false,
                    hidden = false,
                    imageUrl = null,
                    visibleToUser = true,
                    createdAt = "15분 전"
                )
            ),
            hasMoreComments = false,
            totalCommentCount = 3L,
            commentImageUri = null,
            commentImageUrl = null,
            editingCommentId = null,
            editingCommentContent = "",
            editingCommentImageUrl = null,
            editingCommentIsSecret = false
        )
    }
}
