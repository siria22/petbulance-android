package com.petbulance.data.mapper.feature.community

import com.petbulance.data.datasource.remote.network.feature.community.comment.dto.CommentResDto
import com.petbulance.data.datasource.remote.network.feature.community.comment.dto.DelCommentResDto
import com.petbulance.data.datasource.remote.network.feature.community.comment.dto.MyCommentListResDto
import com.petbulance.data.datasource.remote.network.feature.community.comment.dto.PagingMyCommentListResDto
import com.petbulance.data.datasource.remote.network.feature.community.comment.dto.SearchPostCommentListResDto
import com.petbulance.data.datasource.remote.network.feature.community.comment.dto.SearchPostCommentResDto
import com.petbulance.data.datasource.remote.network.feature.community.comment.dto.UpdatePostCommentReqDto
import com.petbulance.domain.model.feature.community.comment.DelComment
import com.petbulance.domain.model.feature.community.comment.MyCommentList
import com.petbulance.domain.model.feature.community.comment.MyCommentListRes
import com.petbulance.domain.model.feature.community.comment.PostComment
import com.petbulance.domain.model.feature.community.comment.SearchPostCommentListRes
import com.petbulance.domain.model.feature.community.comment.SearchPostCommentRes
import com.petbulance.domain.model.feature.community.comment.UpdatePostCommentReq


fun CommentResDto.toDomain(): PostComment = PostComment(
    commentId = commentId,
    content = content,
    parentId = parentId,
    mentionUserNickname = mentionUserNickname,
    isSecret = isSecret,
    imageUrl = imageUrl,
    createdAt = createdAt
)

fun UpdatePostCommentReq.toDto(): UpdatePostCommentReqDto = UpdatePostCommentReqDto(
    content = content,
    imageUrl = imageUrl,
    isSecret = isSecret
)

fun DelCommentResDto.toDomain(): DelComment = DelComment(message = message)

fun SearchPostCommentListResDto.toDomain(): SearchPostCommentListRes =
    SearchPostCommentListRes(
        content = content.map { it.toDomain() },
        hasNext = hasNext,
        totalCount = totalCount
    )

fun SearchPostCommentResDto.toDomain(): SearchPostCommentRes = SearchPostCommentRes(
    commentId = commentId,
    boardId = boardId,
    boardName = boardName,
    postId = postId,
    postTitle = postTitle,
    writerNickname = writerNickname,
    commentContent = commentContent,
    createdAt = createdAt
)

fun PagingMyCommentListResDto.toDomain(): MyCommentList = MyCommentList(
    content = content.map { it.toDomain() },
    hasNext = hasNext
)

fun MyCommentListResDto.toDomain(): MyCommentListRes = MyCommentListRes(
    commentId = commentId,
    boardId = boardId,
    postId = postId,
    postTitle = postTitle,
    commentContent = commentContent,
    hidden = hidden
)
