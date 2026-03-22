package com.petbulance.domain.model.feature.community.post

data class PagingCommentList(
    val items: List<Comment>,
    val hasNext: Boolean,
    val totalCount: Long
)

data class Comment(
    val isRoot: Boolean,
    val commentId: Long,
    val parentId: Long,
    val writerInfo: WriterInfo,
    val mentionUserNickname: String?,
    val content: String?,
    val isSecret: Boolean,
    val isCommentFromPostAuthor: Boolean,
    val isCommentAuthor: Boolean,
    val deleted: Boolean,
    val hidden: Boolean,
    val imageUrl: String?,
    val visibleToUser: Boolean,
    val createdAt: String?
) {
    companion object {
        val stubs = listOf(
            Comment(
                isRoot = true,
                commentId = 1L,
                parentId = 0L,
                writerInfo = WriterInfo("사용자1", null),
                mentionUserNickname = null,
                content = "첫 번째 댓글입니다. 아주 긴 댓글을 작성해보겠습니다. 이 댓글은 여러 줄로 표시될 수 있습니다.",
                isSecret = false,
                isCommentFromPostAuthor = true,
                isCommentAuthor = false,
                deleted = false,
                hidden = false,
                imageUrl = null,
                visibleToUser = true,
                createdAt = "10분 전"
            ),
            Comment(
                isRoot = false,
                commentId = 2L,
                parentId = 1L,
                writerInfo = WriterInfo("사용자2", null),
                mentionUserNickname = "사용자1",
                content = "첫 번째 댓글의 답글입니다.",
                isSecret = false,
                isCommentFromPostAuthor = false,
                isCommentAuthor = true,
                deleted = false,
                hidden = false,
                imageUrl = null,
                visibleToUser = true,
                createdAt = "5분 전"
            ),
            Comment(
                isRoot = true,
                commentId = 3L,
                parentId = 0L,
                writerInfo = WriterInfo("사용자3", null),
                mentionUserNickname = null,
                content = "비밀 댓글입니다.",
                isSecret = true,
                isCommentFromPostAuthor = false,
                isCommentAuthor = false,
                deleted = false,
                hidden = false,
                imageUrl = null,
                visibleToUser = false,
                createdAt = "1분 전"
            ),
            Comment(
                isRoot = true,
                commentId = 4L,
                parentId = 0L,
                writerInfo = WriterInfo("사용자4", null),
                mentionUserNickname = null,
                content = null,
                isSecret = false,
                isCommentFromPostAuthor = false,
                isCommentAuthor = false,
                deleted = true,
                hidden = false,
                imageUrl = null,
                visibleToUser = true,
                createdAt = "1분 전"
            )
        )
    }
}