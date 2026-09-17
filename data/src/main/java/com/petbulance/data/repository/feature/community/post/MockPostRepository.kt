package com.petbulance.data.repository.feature.community.post

import com.petbulance.data.repository.MockFixtures
import com.petbulance.data.repository.MockPost
import com.petbulance.domain.model.feature.community.post.BoardInfo
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
import com.petbulance.domain.model.feature.community.post.PostDetailInfo
import com.petbulance.domain.model.feature.community.post.PostLike
import com.petbulance.domain.model.feature.community.post.PostSearchSummary
import com.petbulance.domain.model.feature.community.post.PostStats
import com.petbulance.domain.model.feature.community.post.PostSummary
import com.petbulance.domain.model.feature.community.post.UserInteraction
import com.petbulance.domain.model.feature.community.post.WriterInfo
import com.petbulance.domain.model.feature.community.post.param.CreateCommentParam
import com.petbulance.domain.model.feature.community.post.param.CreatePostParam
import com.petbulance.domain.model.feature.community.post.param.UpdatePostParam
import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.domain.repository.feature.community.PostRepository
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import javax.inject.Inject

class MockPostRepository @Inject constructor() : PostRepository {
    private var postIdCounter = MockFixtures.posts.maxOf { it.id } + 1
    private var commentIdCounter = MockFixtures.comments.maxOf { it.id } + 1

    override suspend fun createPost(param: CreatePostParam): Result<Post> {
        delay(MockFixtures.NETWORK_DELAY_MS)
        return Result.success(
            Post(
                postId = postIdCounter++,
                type = param.type,
                topic = param.topic,
                title = param.title,
                content = param.content,
                imageUrls = param.imageUrls,
                updatedAt = LocalDateTime.now()
            )
        )
    }

    override suspend fun getPostDetail(postId: Long): Result<PostDetail> {
        delay(MockFixtures.NETWORK_DELAY_MS)
        val post = MockFixtures.findPost(postId)
            ?: return Result.failure(NoSuchElementException("Post not found: $postId"))
        return Result.success(
            PostDetail(
                boardInfo = BoardInfo(
                    id = post.type.ordinal.toLong(),
                    name = post.typeLabel,
                    category = post.topicLabel
                ),
                postInfo = PostDetailInfo(
                    id = post.id,
                    title = post.title,
                    writer = WriterInfo(nickname = post.writer, profileUrl = null),
                    createdAt = MockFixtures.createdAtLabel(post.minutesAgo),
                    content = post.content,
                    images = emptyList(),
                    stats = PostStats(
                        likeCount = post.likeCount,
                        commentCount = post.commentCount,
                        viewCount = post.viewCount
                    ),
                    userInteraction = UserInteraction(isLiked = post.isLiked, isMine = post.isMine)
                )
            )
        )
    }

    override suspend fun updatePost(postId: Long, param: UpdatePostParam): Result<Post> {
        delay(MockFixtures.NETWORK_DELAY_MS)
        val type = MockFixtures.findPost(postId)?.type ?: AnimalCategory.SMALLMAMMALS
        return Result.success(
            Post(
                postId = postId,
                type = type.name,
                topic = param.topic,
                title = param.title,
                content = param.content,
                imageUrls = param.imagesToKeepOrAdd.map { it.imageUrl },
                updatedAt = LocalDateTime.now()
            )
        )
    }

    override suspend fun deletePost(postId: Long): Result<DeletedPost> {
        delay(MockFixtures.NETWORK_DELAY_MS)
        return Result.success(
            DeletedPost(
                postId = postId,
                boardId = MockFixtures.findPost(postId)?.type?.ordinal?.toLong() ?: 0L,
                deleted = true,
                hidden = false,
                deletedAt = LocalDateTime.now().withNano(0).toString()
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
        delay(MockFixtures.NETWORK_DELAY_MS)
        val matched = MockFixtures.posts
            .filter { post -> post.matchesBoard(type, topic) }
            .sortedWith(sort.toComparator())
        val (page, hasNext) = matched.page(lastPostId, pageSize)

        return Result.success(
            PagingPostList(
                noticeBanner = if (lastPostId == null) noticeBanner() else null,
                items = page.map { it.toSummary() },
                hasNext = hasNext
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
        delay(MockFixtures.NETWORK_DELAY_MS)
        val keyword = searchKeyword.trim()
        val matched = MockFixtures.posts
            .filter { post -> post.matchesBoard(type, topic) && post.matchesKeyword(keyword, searchScope) }
            .sortedWith(sort.toComparator())
        val (page, hasNext) = matched.page(lastPostId, pageSize)

        return Result.success(
            PagingPostSearchList(
                items = page.map { it.toSearchSummary() },
                hasNext = hasNext,
                totalPostCount = matched.size.toLong()
            )
        )
    }

    override suspend fun getMyPostList(
        keyword: String?,
        lastPostId: Long?,
        pageSize: Int
    ): Result<PagingMyPostList> {
        delay(MockFixtures.NETWORK_DELAY_MS)
        val matched = MockFixtures.posts
            .filter { it.isMine && (keyword.isNullOrBlank() || it.title.contains(keyword.trim())) }
            .sortedByDescending { it.id }
        val (page, hasNext) = matched.page(lastPostId, pageSize)

        return Result.success(
            PagingMyPostList(
                items = page.map { post ->
                    MyPostSummary(
                        postId = post.id,
                        title = post.title,
                        content = post.content,
                        createdAt = MockFixtures.createdAtLabel(post.minutesAgo),
                        viewCount = post.viewCount.toLong(),
                        likeCount = post.likeCount.toLong(),
                        thumbnailUrl = null,
                        hidden = false
                    )
                },
                hasNext = hasNext
            )
        )
    }

    override suspend fun likePost(postId: Long): Result<PostLike> {
        val likeCount = MockFixtures.findPost(postId)?.likeCount ?: 0
        return Result.success(PostLike(postId = postId, currentLikeCount = likeCount + 1L, isLiked = true))
    }

    override suspend fun unlikePost(postId: Long): Result<PostLike> {
        val likeCount = MockFixtures.findPost(postId)?.likeCount ?: 0
        return Result.success(
            PostLike(postId = postId, currentLikeCount = (likeCount - 1L).coerceAtLeast(0L), isLiked = false)
        )
    }

    override suspend fun createComment(
        postId: Long,
        param: CreateCommentParam
    ): Result<PostCommentRes> {
        delay(MockFixtures.NETWORK_DELAY_MS)
        return Result.success(
            PostCommentRes(
                commentId = commentIdCounter++,
                content = param.content,
                parentId = param.parentId,
                mentionUserNickname = param.mentionUserNickname,
                isSecret = param.isSecret,
                imageUrl = param.imageUrl,
                createdAt = MockFixtures.createdAtLabel(0)
            )
        )
    }

    override suspend fun getCommentList(
        postId: Long,
        lastParentCommentId: Long?,
        lastCommentId: Long?,
        pageSize: Int
    ): Result<PagingCommentList> {
        delay(MockFixtures.NETWORK_DELAY_MS)
        val postWriter = MockFixtures.findPost(postId)?.writer
        // 다음 페이지 요청에는 빈 목록을 돌려준다. 게시글당 댓글이 한 페이지를 넘지 않는다.
        if (lastCommentId != null) {
            return Result.success(PagingCommentList(items = emptyList(), hasNext = false, totalCount = 0))
        }
        val comments = MockFixtures.commentsOf(postId)

        return Result.success(
            PagingCommentList(
                items = comments.map { comment ->
                    Comment(
                        isRoot = comment.isRoot,
                        commentId = comment.id,
                        parentId = comment.parentId,
                        writerInfo = WriterInfo(nickname = comment.writer, profileUrl = null),
                        mentionUserNickname = comment.mentionNickname,
                        content = comment.content,
                        isSecret = false,
                        isCommentFromPostAuthor = comment.writer == postWriter,
                        isCommentAuthor = comment.isMine,
                        deleted = false,
                        hidden = false,
                        imageUrl = null,
                        visibleToUser = true,
                        createdAt = MockFixtures.createdAtLabel(comment.minutesAgo)
                    )
                },
                hasNext = false,
                totalCount = comments.size.toLong()
            )
        )
    }

    private fun noticeBanner(): NoticeBanner? = MockFixtures.notices.maxByOrNull { it.id }?.let { notice ->
        NoticeBanner(
            noticeId = notice.id,
            noticeStatus = NOTICE_BANNER_STATUS,
            title = notice.title,
            content = notice.content
        )
    }

    private fun MockPost.matchesBoard(type: String?, topic: String?): Boolean =
        (type.isNullOrBlank() || this.type.name == type) && (topic.isNullOrBlank() || this.topic.name == topic)

    private fun MockPost.matchesKeyword(keyword: String, scope: String): Boolean {
        if (keyword.isEmpty()) return true
        return when (scope) {
            SCOPE_TITLE -> title.contains(keyword)
            SCOPE_CONTENT -> content.contains(keyword)
            SCOPE_WRITER -> writer.contains(keyword)
            else -> title.contains(keyword) || content.contains(keyword)
        }
    }

    private fun String.toComparator(): Comparator<MockPost> = when (this) {
        SORT_POPULAR -> compareByDescending<MockPost> { it.likeCount }.thenByDescending { it.id }
        SORT_COMMENT -> compareByDescending<MockPost> { it.commentCount }.thenByDescending { it.id }
        else -> compareByDescending { it.id }
    }

    /** 정렬된 목록에서 lastPostId 다음부터 pageSize개를 잘라내고, 다음 페이지 여부를 함께 돌려준다. */
    private fun List<MockPost>.page(lastPostId: Long?, pageSize: Int): Pair<List<MockPost>, Boolean> {
        val startIndex = lastPostId?.let { id -> indexOfFirst { it.id == id } + 1 } ?: 0
        val page = drop(startIndex).take(pageSize)
        return page to (startIndex + page.size < size)
    }

    private fun MockPost.toSummary() = PostSummary(
        id = id,
        type = typeLabel,
        topic = topicLabel,
        title = title,
        content = content,
        thumbnailUrl = null,
        imageCount = 0,
        viewCount = viewCount,
        commentCount = commentCount,
        likeCount = likeCount,
        createdAt = MockFixtures.createdAtLabel(minutesAgo),
        isLiked = isLiked,
        nickname = writer
    )

    private fun MockPost.toSearchSummary() = PostSearchSummary(
        id = id,
        type = typeLabel,
        topic = topicLabel,
        title = title,
        content = content,
        thumbnailUrl = null,
        imageCount = 0,
        viewCount = viewCount,
        commentCount = commentCount,
        likeCount = likeCount,
        createdAt = MockFixtures.createdAtLabel(minutesAgo),
        writerNickname = writer,
        isLiked = isLiked
    )

    companion object {
        private const val NOTICE_BANNER_STATUS = "ACTIVE"
        private const val SORT_POPULAR = "popular"
        private const val SORT_COMMENT = "comment"
        private const val SCOPE_TITLE = "title"
        private const val SCOPE_CONTENT = "content"
        private const val SCOPE_WRITER = "writer"
    }
}
