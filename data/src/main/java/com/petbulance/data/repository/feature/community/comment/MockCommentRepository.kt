package com.petbulance.data.repository.feature.community.comment

import com.petbulance.domain.model.feature.community.comment.DelComment
import com.petbulance.domain.model.feature.community.comment.MyCommentList
import com.petbulance.domain.model.feature.community.comment.MyCommentListRes
import com.petbulance.domain.model.feature.community.comment.PostComment
import com.petbulance.domain.model.feature.community.comment.SearchPostCommentListRes
import com.petbulance.domain.model.feature.community.comment.SearchPostCommentRes
import com.petbulance.domain.model.feature.community.comment.UpdatePostCommentReq
import com.petbulance.domain.repository.feature.community.CommentRepository
import jakarta.inject.Inject
import java.time.LocalDateTime

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
        return Result.success(
            SearchPostCommentListRes(
                content = mockSearchPostCommentList,
                hasNext = true,
                totalCount = 3
            )
        )
    }

    override suspend fun getMyCommentList(
        searchKeyword: String?,
        lastCommentId: Long?,
        pageSize: Int
    ): Result<MyCommentList> {
        return Result.success(
            MyCommentList(
                items = mockMyCommentList,
                hasNext = true
            )
        )
    }
}

private val mockSearchPostCommentList = listOf(
    SearchPostCommentRes(
        commentId = 101L,
        boardId = 1L,
        boardName = "질문과 답변",
        postId = 50L,
        postTitle = "강아지가 사료를 안 먹어요",
        writerNickname = "댕댕이맘",
        commentContent = "혹시 간식을 너무 많이 주신 건 아닌가요? 저희 애도 그랬거든요.",
        createdAt = "2024-12-07 14:30"
    ),
    SearchPostCommentRes(
        commentId = 102L,
        boardId = 3L,
        boardName = "병원 방문 후기",
        postId = 52L,
        postTitle = "서울 XX동물병원 과잉진료 의심",
        writerNickname = "수의사조무사",
        commentContent = "저도 거기 갔다가 비용 폭탄 맞았어요. 비추합니다.",
        createdAt = "2024-12-06 09:15"
    ),
    SearchPostCommentRes(
        commentId = 103L,
        boardId = 6L,
        boardName = "중고장터",
        postId = 110L,
        postTitle = "고양이 캣타워 나눔합니다",
        writerNickname = "냥냥펀치",
        commentContent = "제가 가져가도 될까요? 쪽지 드렸습니다!",
        createdAt = "2024-12-07 15:00"
    )
)

private val mockMyCommentList = listOf(
    MyCommentListRes(
        commentId = 1L,
        boardId = 1L,
        postId = 1L,
        postTitle = "첫 번째 게시글 제목",
        commentContent = "이것은 첫 번째 목업 댓글입니다.",
        createdAt = "2024-12-07",
        hidden = false
    ),
    MyCommentListRes(
        commentId = 2L,
        boardId = 1L,
        postId = 1L,
        postTitle = "첫 번째 게시글 제목",
        commentContent = "이것은 두 번째 목업 댓글입니다.",
        createdAt = "2024-12-06",
        hidden = false
    ),
    MyCommentListRes(
        commentId = 3L,
        boardId = 2L,
        postId = 2L,
        postTitle = "두 번째 게시글 제목",
        commentContent = "이것은 세 번째 목업 댓글입니다. 이 댓글은 숨김 처리됩니다.",
        createdAt = "2024-12-05",
        hidden = true
    )
)