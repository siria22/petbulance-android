package com.example.data.repository.feature.community.post

import com.example.domain.model.feature.community.post.BoardInfo
import com.example.domain.model.feature.community.post.Comment
import com.example.domain.model.feature.community.post.DeletedPost
import com.example.domain.model.feature.community.post.MyPostSummary
import com.example.domain.model.feature.community.post.PagingCommentList
import com.example.domain.model.feature.community.post.PagingMyPostList
import com.example.domain.model.feature.community.post.PagingPostList
import com.example.domain.model.feature.community.post.PagingPostSearchList
import com.example.domain.model.feature.community.post.Post
import com.example.domain.model.feature.community.post.PostCommentRes
import com.example.domain.model.feature.community.post.PostDetail
import com.example.domain.model.feature.community.post.PostDetailInfo
import com.example.domain.model.feature.community.post.PostImage
import com.example.domain.model.feature.community.post.PostLike
import com.example.domain.model.feature.community.post.PostSearchSummary
import com.example.domain.model.feature.community.post.PostStats
import com.example.domain.model.feature.community.post.PostSummary
import com.example.domain.model.feature.community.post.UserInteraction
import com.example.domain.model.feature.community.post.WriterInfo
import com.example.domain.model.feature.community.post.param.CreateCommentParam
import com.example.domain.model.feature.community.post.param.CreatePostParam
import com.example.domain.model.feature.community.post.param.UpdatePostParam
import com.example.domain.repository.feature.community.PostRepository
import java.time.LocalDateTime

class MockPostRepository : PostRepository {
    private var postIdCounter = 1L
    private var commentIdCounter = 1L

    override suspend fun createPost(param: CreatePostParam): Result<Post> {
        return Result.success(
            Post(
                postId = postIdCounter++,
                boardId = param.boardId,
                category = param.category,
                title = param.title,
                content = param.content,
                imageUrls = param.imageUrls,
                updatedAt = LocalDateTime.now()
            )
        )
    }

    override suspend fun getPostDetail(postId: Long): Result<PostDetail> {
        return Result.success(
            PostDetail(
                boardInfo = BoardInfo(id = 1L, name = "자유게시판", category = "정보"),
                postInfo = PostDetailInfo(
                    id = postId,
                    title = "게시글 제목 $postId",
                    writer = WriterInfo(nickname = "작성자", profileUrl = null),
                    createdAt = "2023-10-27T15:00:00",
                    content = "게시글 내용입니다.",
                    images = listOf(PostImage(url = "url", order = 1, isThumbnail = true)),
                    stats = PostStats(likeCount = 10, commentCount = 5, viewCount = 100),
                    userInteraction = UserInteraction(isLiked = false, isMine = true)
                )
            )
        )
    }

    override suspend fun updatePost(postId: Long, param: UpdatePostParam): Result<Post> {
        return Result.success(
            Post(
                postId = postId,
                boardId = 1L, // Mock boardId
                category = param.category,
                title = param.title,
                content = param.content,
                imageUrls = param.imagesToKeepOrAdd.map { it.imageUrl },
                updatedAt = LocalDateTime.now()
            )
        )
    }

    override suspend fun deletePost(postId: Long): Result<DeletedPost> {
        return Result.success(
            DeletedPost(
                postId = postId,
                boardId = 1L,
                deleted = true,
                hidden = false,
                deletedAt = "2023-10-27T15:00:00"
            )
        )
    }

    override suspend fun getPostList(
        boardId: Long?,
        category: String?,
        sort: String,
        lastPostId: Long?,
        pageSize: Int
    ): Result<PagingPostList> {
        val items = List(3) { i ->
            PostSummary(
                id = (lastPostId ?: 0) + i + 1,
                title = "게시글 제목 ${i + 1}",
                summary = "게시글 요약...",
                thumbnailUrl = null,
                commentCount = i * 2,
                likeCount = i * 5
            )
        }
        return Result.success(PagingPostList(items = items, hasNext = true))
    }

    override suspend fun getPostSearchList(
        boardId: Long?,
        categories: List<String>?,
        sort: String,
        lastPostId: Long?,
        pageSize: Int,
        searchKeyword: String?,
        searchScope: String
    ): Result<PagingPostSearchList> {
        val items = List(3) { i ->
            PostSearchSummary(
                id = (lastPostId ?: 0) + i + 1,
                title = "검색된 게시글 ${i + 1}",
                contentSnippet = "내용 스니펫...",
                boardName = "자유게시판",
                createdAt = "2023-10-27T15:00:00"
            )
        }
        return Result.success(
            PagingPostSearchList(
                items = items,
                hasNext = true,
                totalPostCount = 100
            )
        )
    }

    override suspend fun getMyPostList(
        keyword: String?,
        lastPostId: Long?,
        pageSize: Int
    ): Result<PagingMyPostList> {
        val items = List(3) { i ->
            MyPostSummary(
                postId = (lastPostId ?: 0) + i + 1,
                boardId = 1L,
                title = "내가 쓴 글 ${i + 1}",
                content = "내용...",
                createdAt = "2023-10-27T15:00:00",
                viewCount = (i + 1) * 10L,
                hidden = false
            )
        }
        return Result.success(PagingMyPostList(items = items, hasNext = true))
    }

    override suspend fun likePost(postId: Long): Result<PostLike> {
        return Result.success(PostLike(postId = postId, currentLikeCount = 11, isLiked = true))
    }

    override suspend fun unlikePost(postId: Long): Result<PostLike> {
        return Result.success(PostLike(postId = postId, currentLikeCount = 10, isLiked = false))
    }

    override suspend fun createComment(
        postId: Long,
        param: CreateCommentParam
    ): Result<PostCommentRes> {
        return Result.success(
            PostCommentRes(
                commentId = commentIdCounter++,
                content = param.content,
                parentId = param.parentId,
                mentionUserNickname = param.mentionUserNickname,
                isSecret = param.isSecret,
                imageUrl = param.imageUrl,
                createdAt = "2023-10-27T15:00:00"
            )
        )
    }

    override suspend fun getCommentList(
        postId: Long,
        lastParentCommentId: Long?,
        lastCommentId: Long?,
        pageSize: Int
    ): Result<PagingCommentList> {
        val items = List(3) { i ->
            Comment(
                isRoot = true,
                commentId = (lastCommentId ?: 0) + i + 1,
                parentId = (lastParentCommentId ?: 0),
                writerInfo = WriterInfo(nickname = "댓글작성자${i + 1}", profileUrl = null),
                mentionUserNickname = "",
                content = "댓글 내용 ${i + 1}",
                isSecret = false,
                isCommentFromPostAuthor = false,
                isCommentAuthor = true,
                deleted = false,
                hidden = false,
                imageUrl = "",
                visibleToUser = true,
                createdAt = "2023-10-27T15:00:00"
            )
        }
        return Result.success(PagingCommentList(items = items, hasNext = true, totalCount = 30))
    }
}