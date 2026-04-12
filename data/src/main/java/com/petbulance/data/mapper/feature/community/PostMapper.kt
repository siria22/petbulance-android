package com.petbulance.data.mapper.feature.community

import com.petbulance.data.datasource.remote.network.feature.community.post.dto.comment.CreatePostCommentReqDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.comment.PagingPostCommentListResDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.comment.PostCommentListResDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.comment.PostCommentResDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.like.PostLikeDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.post.CreatePostReqDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.post.CreatePostResDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.post.DeletePostResDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.post.DetailPostResDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.post.ImageUpdateDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.post.MyPostListResDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.post.NoticeBannerInfoDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.post.PagingMyPostListResDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.post.PagingPostListResDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.post.PagingPostSearchListResDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.post.PostListResDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.post.PostSearchListResDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.post.UpdatePostReqDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.post.UpdatePostResDto
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
import com.petbulance.domain.model.feature.community.post.PostImage
import com.petbulance.domain.model.feature.community.post.PostLike
import com.petbulance.domain.model.feature.community.post.PostSearchSummary
import com.petbulance.domain.model.feature.community.post.PostStats
import com.petbulance.domain.model.feature.community.post.PostSummary
import com.petbulance.domain.model.feature.community.post.UserInteraction
import com.petbulance.domain.model.feature.community.post.WriterInfo
import com.petbulance.domain.model.feature.community.post.param.CreateCommentParam
import com.petbulance.domain.model.feature.community.post.param.CreatePostParam
import com.petbulance.domain.model.feature.community.post.param.ImageUpdateParam
import com.petbulance.domain.model.feature.community.post.param.UpdatePostParam
import java.time.LocalDateTime

fun CreatePostParam.toDto() = CreatePostReqDto(
    type = type,
    topic = topic,
    title = title,
    content = content,
    imageUrls = imageUrls
)

fun CreatePostResDto.toDomain() = Post(
    postId = postId,
    type = type,
    topic = topic,
    title = title,
    content = content,
    imageUrls = imageUrls,
    updatedAt = LocalDateTime.now()
)

fun DetailPostResDto.toDomain() = PostDetail(
    boardInfo = BoardInfo(
        id = 0L,
        name = post.type,
        category = post.topic
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
    type = type,
    topic = topic,
    title = title,
    content = content,
    imageUrls = imageUrls,
    updatedAt = LocalDateTime.now()
)

fun UpdatePostParam.toDto() = UpdatePostReqDto(
    topic = topic,
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
    boardId = null,
    deleted = deleted,
    hidden = hidden,
    deletedAt = deletedAt
)

fun PagingPostListResDto.toDomain() = PagingPostList(
    noticeBanner = noticeBanner?.toDomain(),
    items = content.map { it.toDomain() },
    hasNext = hasNext
)

fun NoticeBannerInfoDto.toDomain() = NoticeBanner(
    noticeId = noticeId,
    noticeStatus = noticeStatus,
    title = title,
    content = content
)

fun PostListResDto.toDomain() = PostSummary(
    id = postId,
    type = type,
    topic = topic,
    title = title,
    content = content,
    thumbnailUrl = thumbnailUrl,
    imageCount = imageCount.toInt(),
    viewCount = viewCount.toInt(),
    commentCount = commentCount.toInt(),
    likeCount = likeCount.toInt(),
    createdAt = createdAt,
    isLiked = likedByUser,
    nickname = nickname
)

fun PostSearchListResDto.toDomain() = PostSearchSummary(
    id = postId,
    type = type,
    topic = topic,
    title = title,
    content = content,
    thumbnailUrl = thumbnailUrl,
    imageCount = imageCount,
    viewCount = viewCount,
    commentCount = commentCount,
    likeCount = likeCount,
    createdAt = createdAt,
    writerNickname = writerNickname,
    isLiked = likedByUser,
)

fun PagingPostSearchListResDto.toDomain() = PagingPostSearchList(
    items = content.map { it.toDomain() },
    hasNext = hasNext,
    totalPostCount = lastPostId
)

fun PagingMyPostListResDto.toDomain() = PagingMyPostList(
    items = content.map { it.toDomain() },
    hasNext = hasNext
)

fun MyPostListResDto.toDomain() = MyPostSummary(
    postId = postId,
    title = title,
    content = content,
    createdAt = createdAt,
    viewCount = viewCount,
    likeCount = likeCount,
    thumbnailUrl = thumbnailUrl,
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


