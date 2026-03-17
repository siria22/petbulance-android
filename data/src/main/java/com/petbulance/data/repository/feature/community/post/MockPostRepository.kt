package com.petbulance.data.repository.feature.community.post

import com.petbulance.domain.model.feature.community.post.Comment
import com.petbulance.domain.model.feature.community.post.DeletedPost
import com.petbulance.domain.model.feature.community.post.MyPostSummary
import com.petbulance.domain.model.feature.community.post.NoticeBanner
import com.petbulance.domain.model.feature.community.post.PagingCommentList
import com.petbulance.domain.model.feature.community.post.PagingMyPostList
import com.petbulance.domain.model.feature.community.post.PagingPostList
import com.petbulance.domain.model.feature.community.post.PagingPostSearchList
import com.petbulance.domain.model.feature.community.post.Post
import com.petbulance.domain.model.feature.community.post.PostCommentRes
import com.petbulance.domain.model.feature.community.post.PostDetail
import com.petbulance.domain.model.feature.community.post.PostLike
import com.petbulance.domain.model.feature.community.post.PostSearchSummary
import com.petbulance.domain.model.feature.community.post.PostSummary
import com.petbulance.domain.model.feature.community.post.WriterInfo
import com.petbulance.domain.model.feature.community.post.param.CreateCommentParam
import com.petbulance.domain.model.feature.community.post.param.CreatePostParam
import com.petbulance.domain.model.feature.community.post.param.UpdatePostParam
import com.petbulance.domain.repository.feature.community.PostRepository
import java.time.LocalDateTime
import javax.inject.Inject

class MockPostRepository @Inject constructor() : PostRepository {
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
        return Result.failure(NotImplementedError("Mock implementation not available"))
    }

    override suspend fun updatePost(postId: Long, param: UpdatePostParam): Result<Post> {
        return Result.success(
            Post(
                postId = postId,
                boardId = 1L,
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
        type: String?,
        topic: String?,
        sort: String,
        lastPostId: Long?,
        pageSize: Int
    ): Result<PagingPostList> {
        val types = listOf("DOG", "CAT", "SMALLMAMMALS", "AVIAN", "REPTILE", "FISH")
        val topics = listOf("HEALTH", "DAILY", "INFORMATION", "QUESTION", "REVIEW")

        val items = List(pageSize.coerceAtMost(10)) { i ->
            val index = (lastPostId?.toInt() ?: 0) + i
            PostSummary(
                id = index.toLong() + 1,
                type = type ?: types[index % types.size],
                topic = topic ?: topics[index % topics.size],
                title = "게시글 제목 ${index + 1}",
                content = "게시글 내용의 요약입니다. 이 게시글은 ${if (type != null) type else "다양한 주제"}에 대한 내용을 담고 있습니다.",
                thumbnailUrl = if (i % 3 == 0) "https://example.com/image${i}.jpg" else null,
                imageCount = if (i % 3 == 0) (i % 5) + 1 else 0,
                viewCount = (index + 1) * 50,
                commentCount = index * 2,
                likeCount = index * 5,
                createdAt = "${index + 1}시간 전",
                isLiked = index % 2 == 0
            )
        }

        val noticeBanner = if (lastPostId == null) {
            NoticeBanner(
                noticeId = 1,
                noticeStatus = "ACTIVE",
                title = "[공지] 서비스 점검 안내",
                content = "안녕하세요. 펫뷸런스입니다. 더 나은 서비스 제공을 위해 점검을 진행합니다."
            )
        } else null

        return Result.success(
            PagingPostList(
                noticeBanner = noticeBanner,
                items = items,
                hasNext = true
            )
        )
    }

    override suspend fun getPostSearchList(
        type: String?,
        topic: String?,
        sort: String,
        lastPostId: Long?,
        pageSize: Int,
        searchKeyword: String,
        searchScope: String
    ): Result<PagingPostSearchList> {
        val items = List(pageSize.coerceAtMost(10)) { i ->
            val index = (lastPostId?.toInt() ?: 0) + i
            PostSearchSummary(
                id = index.toLong() + 1,
                type = type ?: listOf("DOG", "CAT", "SMALLMAMMALS", "AVIAN", "REPTILE", "FISH")[index % 6],
                topic = topic ?: listOf("HEALTH", "DAILY", "INFORMATION", "QUESTION", "REVIEW")[index % 5],
                title = "검색 결과 게시글 ${index + 1} - $searchKeyword",
                content = "검색어 '$searchKeyword'와 관련된 게시글 내용입니다. 이 글은 ${searchScope}에서 검색되었습니다.",
                thumbnailUrl = if (i % 3 == 0) "https://example.com/search_image${i}.jpg" else null,
                imageCount = if (i % 3 == 0) (i % 5) + 1 else 0,
                viewCount = (index + 1) * 30,
                commentCount = index * 2,
                likeCount = index * 3,
                createdAt = "${index + 1}시간 전",
                writerNickname = "작성자${index + 1}",
                isLiked = index % 2 == 0
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
        val items = List(pageSize.coerceAtMost(10)) { i ->
            val index = (lastPostId?.toInt() ?: 0) + i
            MyPostSummary(
                postId = index.toLong() + 1,
                boardId = 1L,
                title = "내가 쓴 글 ${index + 1}${if (keyword != null) " - $keyword" else ""}",
                content = "내가 작성한 게시글의 내용입니다.",
                createdAt = "${index + 1}시간 전",
                viewCount = (index + 1) * 10L,
                hidden = index % 5 == 0
            )
        }
        return Result.success(PagingMyPostList(items = items, hasNext = true))
    }

    override suspend fun likePost(postId: Long): Result<PostLike> {
        return Result.success(
            PostLike(
                postId = postId,
                currentLikeCount = (postId * 5 + 1),
                isLiked = true
            )
        )
    }

    override suspend fun unlikePost(postId: Long): Result<PostLike> {
        return Result.success(
            PostLike(
                postId = postId,
                currentLikeCount = (postId * 5),
                isLiked = false
            )
        )
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
                createdAt = "방금 전"
            )
        )
    }

    override suspend fun getCommentList(
        postId: Long,
        lastParentCommentId: Long?,
        lastCommentId: Long?,
        pageSize: Int
    ): Result<PagingCommentList> {
        val items = List(pageSize.coerceAtMost(10)) { i ->
            val index = (lastCommentId?.toInt() ?: 0) + i
            Comment(
                isRoot = true,
                commentId = index.toLong() + 1,
                parentId = 0L,
                writerInfo = WriterInfo(nickname = "댓글작성자${index + 1}", profileUrl = null),
                mentionUserNickname = "",
                content = "댓글 내용 ${index + 1}",
                isSecret = false,
                isCommentFromPostAuthor = index % 4 == 0,
                isCommentAuthor = true,
                deleted = false,
                hidden = false,
                imageUrl = "",
                visibleToUser = true,
                createdAt = "${index + 1}시간 전"
            )
        }
        return Result.success(PagingCommentList(items = items, hasNext = true, totalCount = 30))
    }
}