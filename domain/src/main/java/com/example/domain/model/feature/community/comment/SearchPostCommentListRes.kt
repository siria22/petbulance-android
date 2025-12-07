package com.example.domain.model.feature.community.comment

data class SearchPostCommentListRes(
    val content: List<SearchPostCommentRes> = emptyList(),
    val hasNext: Boolean = false,
    val totalCount: Long = 0L
)

data class SearchPostCommentRes(
    val commentId: Long,
    val boardId: Long,
    val boardName: String,
    val postId: Long,
    val postTitle: String,
    val writerNickname: String,
    val commentContent: String,
    val createdAt: String
) {
    companion object {
        fun empty() = SearchPostCommentRes(
            commentId = 0L,
            boardId = 0L,
            boardName = "",
            postId = 0L,
            postTitle = "",
            writerNickname = "",
            commentContent = "",
            createdAt = ""
        )
    }
}