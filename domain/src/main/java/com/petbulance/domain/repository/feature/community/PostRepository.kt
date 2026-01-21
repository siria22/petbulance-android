package com.petbulance.domain.repository.feature.community

import com.petbulance.domain.model.feature.community.post.Post
import com.petbulance.domain.model.feature.community.post.DeletedPost
import com.petbulance.domain.model.feature.community.post.PagingCommentList
import com.petbulance.domain.model.feature.community.post.PagingMyPostList
import com.petbulance.domain.model.feature.community.post.PagingPostList
import com.petbulance.domain.model.feature.community.post.PagingPostSearchList
import com.petbulance.domain.model.feature.community.post.PostCommentRes
import com.petbulance.domain.model.feature.community.post.PostDetail
import com.petbulance.domain.model.feature.community.post.PostLike
import com.petbulance.domain.model.feature.community.post.param.CreateCommentParam
import com.petbulance.domain.model.feature.community.post.param.CreatePostParam
import com.petbulance.domain.model.feature.community.post.param.UpdatePostParam

interface PostRepository {

    suspend fun createPost(param: CreatePostParam): Result<Post>

    suspend fun getPostDetail(postId: Long): Result<PostDetail>

    suspend fun updatePost(
        postId: Long,
        param: UpdatePostParam
    ): Result<Post>

    suspend fun deletePost(postId: Long): Result<DeletedPost>

    suspend fun getPostList(
        boardId: Long? = null,
        category: String? = null,
        sort: String = "popular",
        lastPostId: Long? = null,
        pageSize: Int = 10
    ): Result<PagingPostList>

    suspend fun getPostSearchList(
        boardId: Long? = null,
        categories: List<String>? = null,
        sort: String = "popular",
        lastPostId: Long? = null,
        pageSize: Int = 10,
        searchKeyword: String? = null,
        searchScope: String = "title_content"
    ): Result<PagingPostSearchList>

    suspend fun getMyPostList(
        keyword: String? = null,
        lastPostId: Long? = null,
        pageSize: Int = 10
    ): Result<PagingMyPostList>

    suspend fun likePost(postId: Long): Result<PostLike>

    suspend fun unlikePost(postId: Long): Result<PostLike>

    suspend fun createComment(
        postId: Long,
        param: CreateCommentParam
    ): Result<PostCommentRes>

    suspend fun getCommentList(
        postId: Long,
        lastParentCommentId: Long? = null,
        lastCommentId: Long? = null,
        pageSize: Int = 15
    ): Result<PagingCommentList>
}