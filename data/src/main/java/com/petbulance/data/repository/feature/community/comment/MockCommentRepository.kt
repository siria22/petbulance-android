package com.petbulance.data.repository.feature.community.comment

import com.petbulance.data.repository.MockComment
import com.petbulance.data.repository.MockFixtures
import com.petbulance.domain.model.feature.community.comment.DelComment
import com.petbulance.domain.model.feature.community.comment.MyCommentList
import com.petbulance.domain.model.feature.community.comment.MyCommentListRes
import com.petbulance.domain.model.feature.community.comment.PostComment
import com.petbulance.domain.model.feature.community.comment.SearchPostCommentListRes
import com.petbulance.domain.model.feature.community.comment.SearchPostCommentRes
import com.petbulance.domain.model.feature.community.comment.UpdatePostCommentReq
import com.petbulance.domain.repository.feature.community.CommentRepository
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import javax.inject.Inject

class MockCommentRepository @Inject constructor() : CommentRepository {

    override suspend fun updatePostComment(
        commentId: Long,
        request: UpdatePostCommentReq
    ): Result<PostComment> {
        return Result.success(
            PostComment(
                commentId = commentId,
                content = request.content,
                parentId = null,
                mentionUserNickname = null,
                isSecret = request.isSecret,
                imageUrl = request.imageUrl,
                createdAt = LocalDateTime.now()
            )
        )
    }

    override suspend fun deletePostComment(commentId: Long): Result<DelComment> {
        return Result.success(DelComment(message = "삭제되었습니다."))
    }

    override suspend fun searchPostCommentList(
        searchKeyword: String,
        searchScope: String,
        lastCommentId: Long?,
        pageSize: Int,
        topic: String?,
        type: String?
    ): Result<SearchPostCommentListRes> {
        delay(MockFixtures.NETWORK_DELAY_MS)
        val keyword = searchKeyword.trim()
        val matched = MockFixtures.comments
            .filter { comment ->
                val post = MockFixtures.findPost(comment.postId) ?: return@filter false
                (type.isNullOrBlank() || post.type.name == type) &&
                        (topic.isNullOrBlank() || post.topic.name == topic) &&
                        (keyword.isEmpty() || comment.content.contains(keyword) || comment.writer.contains(keyword))
            }
            .sortedBy { it.minutesAgo }
        val (page, hasNext) = matched.page(lastCommentId, pageSize)

        return Result.success(
            SearchPostCommentListRes(
                content = page.mapNotNull { comment ->
                    val post = MockFixtures.findPost(comment.postId) ?: return@mapNotNull null
                    SearchPostCommentRes(
                        commentId = comment.id,
                        boardId = post.type.ordinal.toLong(),
                        boardName = post.boardLabel,
                        postId = post.id,
                        postTitle = post.title,
                        writerNickname = comment.writer,
                        commentContent = comment.content,
                        createdAt = MockFixtures.createdAtLabel(comment.minutesAgo)
                    )
                },
                hasNext = hasNext,
                totalCount = matched.size.toLong()
            )
        )
    }

    override suspend fun getMyCommentList(
        searchKeyword: String?,
        lastCommentId: Long?,
        pageSize: Int
    ): Result<MyCommentList> {
        delay(MockFixtures.NETWORK_DELAY_MS)
        val matched = MockFixtures.comments
            .filter { it.isMine && (searchKeyword.isNullOrBlank() || it.content.contains(searchKeyword.trim())) }
            .sortedBy { it.minutesAgo }
        val (page, hasNext) = matched.page(lastCommentId, pageSize)

        return Result.success(
            MyCommentList(
                items = page.mapNotNull { comment ->
                    val post = MockFixtures.findPost(comment.postId) ?: return@mapNotNull null
                    MyCommentListRes(
                        commentId = comment.id,
                        boardId = post.type.ordinal.toLong(),
                        postId = post.id,
                        postTitle = post.title,
                        commentContent = comment.content,
                        createdAt = MockFixtures.createdAtLabel(comment.minutesAgo),
                        hidden = false
                    )
                },
                hasNext = hasNext
            )
        )
    }

    /** 정렬된 목록에서 lastCommentId 다음부터 pageSize개를 잘라내고, 다음 페이지 여부를 함께 돌려준다. */
    private fun List<MockComment>.page(lastCommentId: Long?, pageSize: Int): Pair<List<MockComment>, Boolean> {
        val startIndex = lastCommentId?.let { id -> indexOfFirst { it.id == id } + 1 } ?: 0
        val page = drop(startIndex).take(pageSize)
        return page to (startIndex + page.size < size)
    }
}
