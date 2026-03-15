package com.petbulance.data.repository.feature.community.post

import com.petbulance.data.datasource.remote.network.feature.community.post.PostApi
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.comment.PagingPostCommentListResDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.comment.PostCommentResDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.like.PostLikeDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.post.CreatePostResDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.post.DeletePostResDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.post.DetailPostResDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.post.PagingMyPostListResDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.post.PagingPostListResDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.post.PagingPostSearchListResDto
import com.petbulance.data.datasource.remote.network.feature.community.post.dto.post.UpdatePostResDto
import com.petbulance.data.datasource.remote.network.common.safeApiCall
import com.petbulance.data.mapper.feature.community.toDomain
import com.petbulance.data.mapper.feature.community.toDto
import com.petbulance.domain.model.feature.community.post.DeletedPost
import com.petbulance.domain.model.feature.community.post.PagingCommentList
import com.petbulance.domain.model.feature.community.post.PagingMyPostList
import com.petbulance.domain.model.feature.community.post.PagingPostList
import com.petbulance.domain.model.feature.community.post.PagingPostSearchList
import com.petbulance.domain.model.feature.community.post.Post
import com.petbulance.domain.model.feature.community.post.PostCommentRes
import com.petbulance.domain.model.feature.community.post.PostDetail
import com.petbulance.domain.model.feature.community.post.PostLike
import com.petbulance.domain.model.feature.community.post.param.CreateCommentParam
import com.petbulance.domain.model.feature.community.post.param.CreatePostParam
import com.petbulance.domain.model.feature.community.post.param.UpdatePostParam
import com.petbulance.domain.repository.feature.community.PostRepository
import javax.inject.Inject

class PostRepositoryImpl @Inject constructor(
    private val api: PostApi
) : PostRepository {
    override suspend fun createPost(param: CreatePostParam): Result<Post> {
        return safeApiCall<CreatePostResDto>(path = "/posts") {
            api.createPost(param.toDto())
        }.map { it.toDomain() }
    }

    override suspend fun getPostDetail(postId: Long): Result<PostDetail> {
        return safeApiCall<DetailPostResDto>(path = "/posts/$postId") {
            api.getPostDetail(postId)
        }.map { it.toDomain() }
    }

    override suspend fun updatePost(
        postId: Long,
        param: UpdatePostParam
    ): Result<Post> {
        return safeApiCall<UpdatePostResDto>(path = "/posts/$postId") {
            api.updatePost(postId, param.toDto())
        }.map { it.toDomain() }
    }

    override suspend fun deletePost(postId: Long): Result<DeletedPost> {
        return safeApiCall<DeletePostResDto>(path = "/posts/$postId") {
            api.deletePost(postId)
        }.map { it.toDomain() }
    }

    override suspend fun getPostList(
        type: String?,
        topic: String?,
        sort: String,
        lastPostId: Long?,
        pageSize: Int
    ): Result<PagingPostList> {
        return safeApiCall<PagingPostListResDto>(path = "/posts") {
            api.getPostList(type, topic, sort, lastPostId, pageSize)
        }.map { it.toDomain() }
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
        return safeApiCall<PagingPostSearchListResDto>(path = "/posts/search") {
            api.getPostSearchList(type, topic, sort, lastPostId, pageSize, searchKeyword, searchScope)
        }.map { it.toDomain() }
    }

    override suspend fun getMyPostList(
        keyword: String?,
        lastPostId: Long?,
        pageSize: Int
    ): Result<PagingMyPostList> {
        return safeApiCall<PagingMyPostListResDto>(path = "/posts/my") {
            api.getMyPostList(keyword, lastPostId, pageSize)
        }.map { it.toDomain() }
    }

    override suspend fun likePost(postId: Long): Result<PostLike> {
        return safeApiCall<PostLikeDto>(path = "/posts/$postId/like") {
            api.likePost(postId)
        }.map { it.toDomain() }
    }

    override suspend fun unlikePost(postId: Long): Result<PostLike> {
        return safeApiCall<PostLikeDto>(path = "/posts/$postId/like") {
            api.unlikePost(postId)
        }.map { it.toDomain() }
    }

    override suspend fun createComment(
        postId: Long,
        param: CreateCommentParam
    ): Result<PostCommentRes> {
        return safeApiCall<PostCommentResDto>(path = "/posts/$postId/comments") {
            api.createComment(postId, param.toDto())
        }.map { it.toDomain() }
    }

    override suspend fun getCommentList(
        postId: Long,
        lastParentCommentId: Long?,
        lastCommentId: Long?,
        pageSize: Int
    ): Result<PagingCommentList> {
        return safeApiCall<PagingPostCommentListResDto>(path = "/posts/$postId/comments") {
            api.getCommentList(postId, lastParentCommentId, lastCommentId, pageSize)
        }.map { it.toDomain() }
    }
}