package com.example.data.repository.feature.community.post

import com.example.data.datasource.remote.network.feature.community.post.PostApi
import com.example.data.datasource.remote.network.feature.community.post.dto.comment.PagingPostCommentListResDto
import com.example.data.datasource.remote.network.feature.community.post.dto.comment.PostCommentResDto
import com.example.data.datasource.remote.network.feature.community.post.dto.like.PostLikeDto
import com.example.data.datasource.remote.network.feature.community.post.dto.post.CreatePostResDto
import com.example.data.datasource.remote.network.feature.community.post.dto.post.DeletePostResDto
import com.example.data.datasource.remote.network.feature.community.post.dto.post.DetailPostResDto
import com.example.data.datasource.remote.network.feature.community.post.dto.post.PagingMyPostListResDto
import com.example.data.datasource.remote.network.feature.community.post.dto.post.PagingPostListResDto
import com.example.data.datasource.remote.network.feature.community.post.dto.post.PagingPostSearchListResDto
import com.example.data.datasource.remote.network.feature.community.post.dto.post.UpdatePostResDto
import com.example.data.datasource.remote.network.common.safeApiCall
import com.example.data.mapper.feature.community.toDomain
import com.example.data.mapper.feature.community.toDto
import com.example.domain.model.feature.community.post.DeletedPost
import com.example.domain.model.feature.community.post.PagingCommentList
import com.example.domain.model.feature.community.post.PagingMyPostList
import com.example.domain.model.feature.community.post.PagingPostList
import com.example.domain.model.feature.community.post.PagingPostSearchList
import com.example.domain.model.feature.community.post.Post
import com.example.domain.model.feature.community.post.PostCommentRes
import com.example.domain.model.feature.community.post.PostDetail
import com.example.domain.model.feature.community.post.PostLike
import com.example.domain.model.feature.community.post.param.CreateCommentParam
import com.example.domain.model.feature.community.post.param.CreatePostParam
import com.example.domain.model.feature.community.post.param.UpdatePostParam
import com.example.domain.repository.feature.community.PostRepository
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
        boardId: Long?,
        category: String?,
        sort: String,
        lastPostId: Long?,
        pageSize: Int
    ): Result<PagingPostList> {
        return safeApiCall<PagingPostListResDto>(path = "/posts") {
            api.getPostList(boardId, category, sort, lastPostId, pageSize)
        }.map { it.toDomain() }
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
        return safeApiCall<PagingPostSearchListResDto>(path = "/posts/search") {
            api.getPostSearchList(boardId, categories, sort, lastPostId, pageSize, searchKeyword, searchScope)
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