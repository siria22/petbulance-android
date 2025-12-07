package com.example.domain.model.feature.community.post

data class PostDetail(
    val boardInfo: BoardInfo,
    val postInfo: PostDetailInfo
)

data class BoardInfo(
    val id: Long,
    val name: String,
    val category: String
)

data class PostDetailInfo(
    val id: Long,
    val title: String,
    val writer: WriterInfo,     // 작성자 정보 그룹화
    val createdAt: String,
    val content: String,
    val images: List<PostImage>,
    val stats: PostStats,       // 숫자 정보 그룹화 (좋아요, 댓글 등)
    val userInteraction: UserInteraction // 유저 상호작용 정보 (좋아요 여부 등)
)

data class WriterInfo(
    val nickname: String,
    val profileUrl: String?
)

data class PostImage(
    val url: String,
    val order: Int,
    val isThumbnail: Boolean
)

data class PostStats(
    val likeCount: Int,
    val commentCount: Int,
    val viewCount: Int
)

data class UserInteraction(
    val isLiked: Boolean,
    val isMine: Boolean // 내가 쓴 글인지 여부
)