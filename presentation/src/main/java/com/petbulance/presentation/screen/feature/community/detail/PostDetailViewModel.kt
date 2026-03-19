package com.petbulance.presentation.screen.feature.community.detail

import androidx.lifecycle.SavedStateHandle
import com.petbulance.domain.model.feature.community.post.PostDetail
import com.petbulance.domain.model.feature.support.report.ReportParam
import com.petbulance.domain.model.type.ReportType
import android.net.Uri
import com.petbulance.domain.model.feature.community.post.param.CreateCommentParam
import com.petbulance.domain.model.nonfeature.app.PresignFileRequest
import com.petbulance.domain.usecase.feature.community.comment.DeleteCommentUseCase
import com.petbulance.domain.usecase.feature.community.post.CreateCommentUseCase
import com.petbulance.domain.usecase.feature.community.post.DeletePostUseCase
import com.petbulance.domain.usecase.feature.community.post.GetCommentListUseCase
import com.petbulance.domain.usecase.feature.community.post.GetPostDetailUseCase
import com.petbulance.domain.usecase.feature.support.report.CreateReportUseCase
import com.petbulance.domain.usecase.nonfeature.app.UploadImageUseCase
import com.petbulance.domain.repository.nonfeature.app.AppInfoRepository
import com.petbulance.presentation.utils.BaseViewModel
import com.petbulance.presentation.utils.error.ErrorDisplayType
import com.petbulance.presentation.utils.nav.ScreenDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class PostDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val deletePostUseCase: DeletePostUseCase,
    private val createReportUseCase: CreateReportUseCase,
    private val getCommentListUseCase: GetCommentListUseCase,
    private val createCommentUseCase: CreateCommentUseCase,
    private val deleteCommentUseCase: DeleteCommentUseCase,
    private val appInfoRepository: AppInfoRepository,
    private val uploadImageUseCase: UploadImageUseCase
) : BaseViewModel() {

    private val postId: Long = savedStateHandle.get<Long>(ScreenDestinations.Community.PostDetail.ARG_ID) ?: 0L

    private val _dataState = MutableStateFlow<PostDetailDataState>(PostDetailDataState.Init)
    val dataState = _dataState.asStateFlow()

    private val _screenState = MutableStateFlow<PostDetailScreenState>(PostDetailScreenState.Init)
    val screenState = _screenState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<PostDetailEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _uploadingImage = MutableStateFlow(false)
    val uploadingImage = _uploadingImage.asStateFlow()

    private val _postDetailData = MutableStateFlow(PostDetailData.empty)
    val postDetailData = _postDetailData.asStateFlow()

    init {
        observeErrorEvent(_eventFlow)
        if (postId == 0L) {
            launch {
                _eventFlow.emit(
                    PostDetailEvent.DataFetch.Error(
                        userMessage = "잘못된 게시글입니다",
                        exceptionMessage = "Invalid postId: 0",
                        displayType = ErrorDisplayType.Common
                    )
                )
            }
        } else {
            loadDetail()
        }
    }

    fun onIntent(intent: PostDetailIntent) {
        when (intent) {
            is PostDetailIntent.LoadDetail -> loadDetail()
            is PostDetailIntent.DeletePost -> deletePost()
            is PostDetailIntent.ReportPost -> reportPost(intent.reason)
            is PostDetailIntent.ToggleLike -> toggleLike()
            is PostDetailIntent.LoadComments -> loadComments()
            is PostDetailIntent.LoadMoreComments -> loadMoreComments()
            is PostDetailIntent.CreateComment -> createComment(intent)
            is PostDetailIntent.DeleteComment -> deleteComment(intent.commentId)
            is PostDetailIntent.ReportComment -> reportComment(intent.commentId, intent.reason)
            is PostDetailIntent.SelectCommentImage -> selectCommentImage(intent.uri)
            is PostDetailIntent.ClearCommentImage -> clearCommentImage()
        }
    }

    private fun loadDetail() {
        launch {
            if (postId == 0L) return@launch

            _dataState.value = PostDetailDataState.OnProgress

            getPostDetailUseCase(postId)
                .onSuccess { postDetail ->
                    _postDetailData.value = postDetail.toUiModel()
                    _dataState.value = PostDetailDataState.Init
                }
                .onFailure { exception ->
                    _eventFlow.emit(
                        PostDetailEvent.DataFetch.Error(
                            userMessage = "게시글을 불러올 수 없습니다",
                            exceptionMessage = exception.message,
                            displayType = ErrorDisplayType.Common
                        )
                    )
                    _dataState.value = PostDetailDataState.Init
                }
        }
    }

    private fun deletePost() {
        launch {
            if (postId == 0L) return@launch

            _dataState.value = PostDetailDataState.OnProgress

            deletePostUseCase(postId)
                .onSuccess {
                    _eventFlow.emit(PostDetailEvent.DeleteSuccess)
                }
                .onFailure { exception ->
                    _eventFlow.emit(
                        PostDetailEvent.DataFetch.Error(
                            userMessage = "게시글 삭제에 실패했습니다",
                            exceptionMessage = exception.message,
                            displayType = ErrorDisplayType.Common
                        )
                    )
                }

            _dataState.value = PostDetailDataState.Init
        }
    }

    private fun reportPost(reason: String) {
        launch {
            if (postId == 0L) return@launch

            _dataState.value = PostDetailDataState.OnProgress

            val param = ReportParam(
                reportType = ReportType.POST,
                reportReason = reason,
                targetId = postId
            )

            createReportUseCase(param)
                .onSuccess {
                    _eventFlow.emit(PostDetailEvent.ReportFirstSuccess)
                }
                .onFailure { exception ->
                    if (exception.message?.contains("ALREADY_REPORTED") == true ||
                        exception.message?.contains("이미 접수된 신고") == true
                    ) {
                        _eventFlow.emit(PostDetailEvent.ReportDuplicate)
                    } else {
                        _eventFlow.emit(
                            PostDetailEvent.DataFetch.Error(
                                userMessage = "신고 접수에 실패했습니다",
                                exceptionMessage = exception.message,
                                displayType = ErrorDisplayType.Common
                            )
                        )
                    }
                }

            _dataState.value = PostDetailDataState.Init
        }
    }

    private fun toggleLike() {
        launch {
            _postDetailData.update { current ->
                current.copy(
                    isLiked = !current.isLiked,
                    likeCount = if (current.isLiked) current.likeCount - 1 else current.likeCount + 1
                )
            }
        }
    }

    private fun loadComments() {
        launch {
            if (postId == 0L) return@launch

            _dataState.value = PostDetailDataState.OnProgress

            getCommentListUseCase(
                postId = postId,
                lastParentCommentId = null,
                lastCommentId = null,
                pageSize = 15
            )
                .onSuccess { pagingCommentList ->
                    _postDetailData.update { current ->
                        current.copy(
                            comments = pagingCommentList.items,
                            hasMoreComments = pagingCommentList.hasNext,
                            totalCommentCount = pagingCommentList.totalCount
                        )
                    }
                    _dataState.value = PostDetailDataState.Init
                }
                .onFailure { exception ->
                    _eventFlow.emit(
                        PostDetailEvent.DataFetch.Error(
                            userMessage = "댓글을 불러올 수 없습니다",
                            exceptionMessage = exception.message,
                            displayType = ErrorDisplayType.Common
                        )
                    )
                    _dataState.value = PostDetailDataState.Init
                }
        }
    }

    private fun loadMoreComments() {
        launch {
            if (postId == 0L) return@launch
            if (!_postDetailData.value.hasMoreComments) return@launch

            val currentComments = _postDetailData.value.comments
            if (currentComments.isEmpty()) return@launch

            val lastParentComment = currentComments.lastOrNull { it.isRoot }
            val lastComment = currentComments.lastOrNull()

            getCommentListUseCase(
                postId = postId,
                lastParentCommentId = lastParentComment?.commentId,
                lastCommentId = lastComment?.commentId,
                pageSize = 15
            )
                .onSuccess { pagingCommentList ->
                    _postDetailData.update { current ->
                        current.copy(
                            comments = current.comments + pagingCommentList.items,
                            hasMoreComments = pagingCommentList.hasNext
                        )
                    }
                }
                .onFailure { exception ->
                    _eventFlow.emit(
                        PostDetailEvent.DataFetch.Error(
                            userMessage = "댓글을 불러올 수 없습니다",
                            exceptionMessage = exception.message,
                            displayType = ErrorDisplayType.Common
                        )
                    )
                }
        }
    }

    private fun createComment(intent: PostDetailIntent.CreateComment) {
        launch {
            if (postId == 0L) return@launch

            _dataState.value = PostDetailDataState.OnProgress

            val param = CreateCommentParam(
                content = intent.content,
                parentId = intent.parentId,
                mentionUserNickname = intent.mentionUserNickname,
                imageUrl = intent.imageUrl,
                isSecret = intent.isSecret
            )

            createCommentUseCase(postId, param)
                .onSuccess {
                    _eventFlow.emit(PostDetailEvent.CommentCreateSuccess)
                    loadDetail()
                    loadComments()
                }
                .onFailure { exception ->
                    _eventFlow.emit(
                        PostDetailEvent.CommentCreateError(
                            userMessage = "댓글 등록에 실패했습니다",
                            exceptionMessage = exception.message,
                            displayType = ErrorDisplayType.Common
                        )
                    )
                    _dataState.value = PostDetailDataState.Init
                }
        }
    }

    private fun deleteComment(commentId: Long) {
        launch {
            _dataState.value = PostDetailDataState.OnProgress

            deleteCommentUseCase(commentId)
                .onSuccess {
                    _eventFlow.emit(PostDetailEvent.CommentDeleteSuccess)
                    loadDetail()
                    loadComments()
                }
                .onFailure { exception ->
                    _eventFlow.emit(
                        PostDetailEvent.CommentDeleteError(
                            userMessage = "댓글 삭제에 실패했습니다",
                            exceptionMessage = exception.message,
                            displayType = ErrorDisplayType.Common
                        )
                    )
                    _dataState.value = PostDetailDataState.Init
                }
        }
    }

    private fun reportComment(commentId: Long, reason: String) {
        launch {
            _dataState.value = PostDetailDataState.OnProgress

            val param = ReportParam(
                reportType = ReportType.COMMENT,
                reportReason = reason,
                targetId = commentId
            )

            createReportUseCase(param)
                .onSuccess {
                    _eventFlow.emit(PostDetailEvent.CommentReportSuccess)
                }
                .onFailure { exception ->
                    if (exception.message?.contains("ALREADY_REPORTED") == true ||
                        exception.message?.contains("이미 접수된 신고") == true
                    ) {
                        _eventFlow.emit(PostDetailEvent.CommentReportDuplicate)
                    } else {
                        _eventFlow.emit(
                            PostDetailEvent.CommentReportError(
                                userMessage = "댓글 신고에 실패했습니다",
                                exceptionMessage = exception.message,
                                displayType = ErrorDisplayType.Common
                            )
                        )
                    }
                }

            _dataState.value = PostDetailDataState.Init
        }
    }

    private fun selectCommentImage(uri: Uri) {
        _postDetailData.update { it.copy(commentImageUri = uri) }
    }

    private fun clearCommentImage() {
        _postDetailData.update { it.copy(commentImageUri = null, commentImageUrl = null) }
    }

    fun uploadCommentImageIfNeeded(context: android.content.Context) {
        launch {
            val uri = _postDetailData.value.commentImageUri ?: return@launch
            
            _uploadingImage.value = true

            try {
                val contentResolver = context.contentResolver
                val mimeType = contentResolver.getType(uri) ?: "image/jpeg"
                val inputStream = contentResolver.openInputStream(uri)
                val imageBytes = inputStream?.readBytes() ?: byteArrayOf()
                inputStream?.close()

                val filename = "comment_${System.currentTimeMillis()}.${mimeType.substringAfter("/")}"
                val presignRequest = listOf(PresignFileRequest(filename, mimeType))

                appInfoRepository.getPresignedUrl(presignRequest)
                    .onSuccess { presignedUrls ->
                        val presignedUrl = presignedUrls.firstOrNull()
                        if (presignedUrl != null) {
                            uploadImageUseCase(
                                presignedUrl.preSignedUrl,
                                imageBytes,
                                mimeType
                            )
                                .onSuccess {
                                    _postDetailData.update { it.copy(commentImageUrl = presignedUrl.imageUrl) }
                                }
                                .onFailure { exception ->
                                    _eventFlow.emit(
                                        PostDetailEvent.CommentCreateError(
                                            userMessage = "이미지 업로드에 실패했습니다",
                                            exceptionMessage = exception.message,
                                            displayType = ErrorDisplayType.Common
                                        )
                                    )
                                }
                        }
                    }
                    .onFailure { exception ->
                        _eventFlow.emit(
                            PostDetailEvent.CommentCreateError(
                                userMessage = "이미지 업로드 준비에 실패했습니다",
                                exceptionMessage = exception.message,
                                displayType = ErrorDisplayType.Common
                            )
                        )
                    }
            } catch (e: Exception) {
                _eventFlow.emit(
                    PostDetailEvent.CommentCreateError(
                        userMessage = "이미지 처리에 실패했습니다",
                        exceptionMessage = e.message,
                        displayType = ErrorDisplayType.Common
                    )
                )
            } finally {
                _uploadingImage.value = false
            }
        }
    }

    private fun PostDetail.toUiModel(): PostDetailData {
        return PostDetailData(
            postId = this.postInfo.id,
            boardName = this.boardInfo.name,
            category = this.boardInfo.category,
            writerNickname = this.postInfo.writer.nickname,
            profileUrl = this.postInfo.writer.profileUrl,
            createdAt = this.postInfo.createdAt,
            title = this.postInfo.title,
            content = this.postInfo.content,
            images = this.postInfo.images.map { it.url },
            likeCount = this.postInfo.stats.likeCount,
            commentCount = this.postInfo.stats.commentCount,
            viewCount = this.postInfo.stats.viewCount,
            isLiked = this.postInfo.userInteraction.isLiked,
            isMine = this.postInfo.userInteraction.isMine,
            comments = emptyList(),
            hasMoreComments = false,
            totalCommentCount = 0L,
            commentImageUri = null,
            commentImageUrl = null
        )
    }
}
