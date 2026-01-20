package com.example.data.mapper.feature.community

import com.example.data.datasource.remote.network.feature.community.post.dto.comment.CreatePostCommentReqDto
import com.example.data.datasource.remote.network.feature.community.post.dto.comment.PagingPostCommentListResDto
import com.example.data.datasource.remote.network.feature.community.post.dto.comment.PostCommentListResDto
import com.example.data.datasource.remote.network.feature.community.post.dto.comment.PostCommentResDto
import com.example.data.datasource.remote.network.feature.community.post.dto.like.PostLikeDto
import com.example.data.datasource.remote.network.feature.community.post.dto.post.CreatePostReqDto
import com.example.data.datasource.remote.network.feature.community.post.dto.post.CreatePostResDto
import com.example.data.datasource.remote.network.feature.community.post.dto.post.DeletePostResDto
import com.example.data.datasource.remote.network.feature.community.post.dto.post.DetailPostResDto
import com.example.data.datasource.remote.network.feature.community.post.dto.post.ImageUpdateDto
import com.example.data.datasource.remote.network.feature.community.post.dto.post.MyPostListResDto
import com.example.data.datasource.remote.network.feature.community.post.dto.post.PagingMyPostListResDto
import com.example.data.datasource.remote.network.feature.community.post.dto.post.PagingPostListResDto
import com.example.data.datasource.remote.network.feature.community.post.dto.post.PagingPostSearchListResDto
import com.example.data.datasource.remote.network.feature.community.post.dto.post.PostListResDto
import com.example.data.datasource.remote.network.feature.community.post.dto.post.PostSearchListResDto
import com.example.data.datasource.remote.network.feature.community.post.dto.post.UpdatePostReqDto
import com.example.data.datasource.remote.network.feature.community.post.dto.post.UpdatePostResDto
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
import com.example.domain.model.feature.community.post.param.ImageUpdateParam
import com.example.domain.model.feature.community.post.param.UpdatePostParam
import java.time.LocalDateTime

fun CreatePostParam.toDto() = CreatePostReqDto(
    boardId = boardId,
    category = category,
    title = title,
    content = content,
    imageUrls = imageUrls
)

fun CreatePostResDto.toDomain() = Post(
    postId = postId,
    boardId = boardId,
    category = category,
    title = title,
    content = content,
    imageUrls = imageUrls,
    updatedAt = LocalDateTime.now()
)

fun DetailPostResDto.toDomain() = PostDetail(
    boardInfo = BoardInfo(
        id = board.boardId,
        name = board.boardName,
        category = board.category
    ),
    postInfo = PostDetailInfo(
        id = post.postId,
        title = post.title,
        writer = WriterInfo(
            nickname = post.writerNickname ?: "",
            profileUrl = post.writerProfileUrl
        ),
        createdAt = post.createdAt,
        content = post.content,
        images = post.images.map {
            PostImage(
                url = it.imageUrl,
                order = it.imageOrder,
                isThumbnail = it.thumbnail
            )
        },
        stats = PostStats(
            likeCount = post.likeCount,
            commentCount = post.commentCount,
            viewCount = post.viewCount
        ),
        userInteraction = UserInteraction(
            isLiked = post.likedByUser,
            isMine = post.isCurrentUserPost
        )
    )
)

fun UpdatePostResDto.toDomain() = Post(
    postId = postId,
    boardId = boardId,
    category = category,
    title = title,
    content = content,
    imageUrls = imageUrls,
    updatedAt = LocalDateTime.now()
)

fun UpdatePostParam.toDto() = UpdatePostReqDto(
    category = category,
    title = title,
    content = content,
    imagesToKeepOrAdd = imagesToKeepOrAdd.map { it.toImageUpdateDto() },
    imageUrlsToDelete = imageUrlsToDelete
)

fun ImageUpdateParam.toImageUpdateDto() = ImageUpdateDto(
    imageUrl = imageUrl,
    imageOrder = imageOrder,
    thumbnail = isThumbnail
)

fun DeletePostResDto.toDomain() = DeletedPost(
    postId = postId,
    boardId = boardId,
    deleted = deleted,
    hidden = hidden,
    deletedAt = deletedAt
)

fun PagingPostListResDto.toDomain() = PagingPostList(
    items = content.map { it.toDomain() },
    hasNext = hasNext
)

fun PostListResDto.toDomain() = PostSummary(
    id = postId,
    boardId = boardId,
    boardName = boardName,
    category = category,
    title = title,
    content = content,
    thumbnailUrl = thumbnailUrl,
    imageCount = imageCount,
    viewCount = viewCount,
    commentCount = commentCount,
    likeCount = likeCount,
    createdAt = createdAt,
    isLiked = likedByUser
)

fun PostSearchListResDto.toDomain() = PostSearchSummary(
    id = postId,
    boardId = boardId,
    boardName = boardName,
    categories = category,
    title = title,
    content = content,
    thumbnailUrl = thumbnailUrl,
    imageCount = imageCount,
    viewCount = viewCount,
    commentCount = commentCount,
    likeCount = likeCount,
    createdAt = createdAt,
    writerNickname = writerNickname,
    writerProfileUrl = writerProfileUrl,
    isLiked = likedByUser
)

fun PagingPostSearchListResDto.toDomain() = PagingPostSearchList(
    items = content.map { it.toDomain() },
    hasNext = hasNext,
    totalPostCount = totalPostCount
)

fun PagingMyPostListResDto.toDomain() = PagingMyPostList(
    items = content.map { it.toDomain() },
    hasNext = hasNext
)

fun MyPostListResDto.toDomain() = MyPostSummary(
    postId = postId,
    boardId = boardId,
    title = title,
    content = content,
    createdAt = createdAt,
    viewCount = viewCount,
    hidden = hidden
)

fun PostLikeDto.toDomain() = PostLike(
    postId = postId,
    currentLikeCount = likeCount,
    isLiked = liked
)

fun CreateCommentParam.toDto() = CreatePostCommentReqDto(
    content = content,
    parentId = parentId,
    mentionUserNickname = mentionUserNickname,
    imageUrl = imageUrl,
    isSecret = isSecret
)

fun PostCommentResDto.toDomain() = PostCommentRes(
    commentId = commentId,
    content = content,
    parentId = parentId,
    mentionUserNickname = mentionUserNickname,
    isSecret = isSecret,
    imageUrl = imageUrl,
    createdAt = createdAt
)

fun PagingPostCommentListResDto.toDomain() = PagingCommentList(
    items = content.map { it.toDomain() },
    hasNext = hasNext,
    totalCount = totalCommentCount
)

fun PostCommentListResDto.toDomain() = Comment(
    isRoot = isRoot,
    commentId = commentId,
    parentId = parentId,
    writerInfo = WriterInfo(
        writerNickname,
        writerProfileUrl
    ),
    mentionUserNickname = mentionUserNickname,
    content = content,
    isSecret = isSecret,
    isCommentFromPostAuthor = isCommentFromPostAuthor,
    isCommentAuthor = isCommentAuthor,
    deleted = deleted,
    hidden = hidden,
    imageUrl = imageUrl,
    visibleToUser = visibleToUser,
    createdAt = createdAt
)


