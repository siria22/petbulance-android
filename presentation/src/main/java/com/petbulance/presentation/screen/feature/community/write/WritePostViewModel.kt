package com.petbulance.presentation.screen.feature.community.write

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import com.petbulance.domain.model.feature.community.post.param.CreatePostParam
import com.petbulance.domain.model.feature.community.post.param.ImageUpdateParam
import com.petbulance.domain.model.feature.community.post.param.UpdatePostParam
import com.petbulance.domain.model.nonfeature.app.PresignFileRequest
import com.petbulance.domain.repository.nonfeature.app.AppInfoRepository
import com.petbulance.domain.repository.nonfeature.app.ContentFileReader
import com.petbulance.domain.usecase.feature.community.post.CreatePostUseCase
import com.petbulance.domain.usecase.feature.community.post.GetPostDetailUseCase
import com.petbulance.domain.usecase.feature.community.post.UpdatePostUseCase
import com.petbulance.domain.usecase.nonfeature.app.UploadImageUseCase
import com.petbulance.presentation.utils.BaseViewModel
import com.petbulance.presentation.utils.nav.ScreenDestinations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class WritePostViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val createPostUseCase: CreatePostUseCase,
    private val updatePostUseCase: UpdatePostUseCase,
    private val getPostDetailUseCase: GetPostDetailUseCase,
    private val appInfoRepository: AppInfoRepository,
    private val uploadImageUseCase: UploadImageUseCase,
    private val contentFileReader: ContentFileReader
) : BaseViewModel() {

    private val postId: Long? = savedStateHandle
        .get<Long>(ScreenDestinations.Community.WritePost.ARG_POST_ID)
        ?.takeIf { it != ScreenDestinations.Community.WritePost.NO_POST_ID }

    private val _dataState = MutableStateFlow<WritePostDataState>(WritePostDataState.Idle)
    val dataState: StateFlow<WritePostDataState> = _dataState.asStateFlow()

    private val _screenState = MutableStateFlow<WritePostScreenState>(WritePostScreenState.Idle)
    val screenState: StateFlow<WritePostScreenState> = _screenState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<WritePostEvent>(extraBufferCapacity = 1)
    val eventFlow: SharedFlow<WritePostEvent> = _eventFlow.asSharedFlow()

    private val _writePostData = MutableStateFlow(WritePostData.empty)
    val writePostData: StateFlow<WritePostData> = _writePostData.asStateFlow()

    init {
        observeErrorEvent(eventFlow)
        if (postId != null) {
            loadPostForEdit(postId)
        }
    }

    fun onIntent(intent: WritePostIntent) {
        when (intent) {
            is WritePostIntent.SelectAnimalType -> selectAnimalType(intent.type)
            is WritePostIntent.SelectTopic -> selectTopic(intent.topic)
            is WritePostIntent.UpdateTitle -> updateTitle(intent.title)
            is WritePostIntent.UpdateContent -> updateContent(intent.content)
            is WritePostIntent.AddImage -> addImage(intent.uri)
            is WritePostIntent.RemoveImage -> removeImage(intent.index)
            is WritePostIntent.NavigateUp -> navigateUp()
        }
    }

    fun submit() {
        val data = _writePostData.value
        if (!data.isSubmitEnabled) return

        _writePostData.update { it.copy(isSubmitting = true) }

        launch {
            val uploadedUrls = uploadNewImages(data.newImageUris)

            when (data.mode) {
                WritePostData.WritePostMode.CREATE -> {
                    val param = CreatePostParam(
                        type = data.selectedAnimalType!!,
                        topic = data.selectedTopic!!,
                        title = data.title,
                        content = data.content,
                        imageUrls = uploadedUrls
                    )
                    createPostUseCase(param).fold(
                        onSuccess = { post ->
                            _eventFlow.emit(WritePostEvent.NavigateToPostDetail(post.postId))
                        },
                        onFailure = { error ->
                            _writePostData.update { it.copy(isSubmitting = false) }
                            _eventFlow.emit(
                                WritePostEvent.SubmitError(
                                    userMessage = "게시글 등록에 실패했습니다",
                                    exceptionMessage = error.message
                                )
                            )
                        }
                    )
                }

                WritePostData.WritePostMode.EDIT -> {
                    val currentPostId = postId ?: return@launch
                    val imagesToKeepOrAdd = data.existingImageUrls.mapIndexed { index, url ->
                        ImageUpdateParam(
                            imageUrl = url,
                            imageOrder = index,
                            isThumbnail = index == 0
                        )
                    } + uploadedUrls.mapIndexed { index, url ->
                        ImageUpdateParam(
                            imageUrl = url,
                            imageOrder = data.existingImageUrls.size + index,
                            isThumbnail = data.existingImageUrls.isEmpty() && index == 0
                        )
                    }
                    val param = UpdatePostParam(
                        topic = data.selectedTopic!!,
                        title = data.title,
                        content = data.content,
                        imagesToKeepOrAdd = imagesToKeepOrAdd,
                        imageUrlsToDelete = data.deletedImageUrls
                    )
                    updatePostUseCase(currentPostId, param).fold(
                        onSuccess = { post ->
                            _eventFlow.emit(WritePostEvent.NavigateToPostDetail(post.postId))
                        },
                        onFailure = { error ->
                            _writePostData.update { it.copy(isSubmitting = false) }
                            _eventFlow.emit(
                                WritePostEvent.SubmitError(
                                    userMessage = "게시글 수정에 실패했습니다",
                                    exceptionMessage = error.message
                                )
                            )
                        }
                    )
                }
            }
        }
    }

    private fun loadPostForEdit(id: Long) {
        launch {
            _dataState.update { WritePostDataState.Loading }
            getPostDetailUseCase(id).fold(
                onSuccess = { postDetail ->
                    _writePostData.update {
                        it.copy(
                            mode = WritePostData.WritePostMode.EDIT,
                            selectedAnimalType = postDetail.boardInfo.name,
                            selectedTopic = postDetail.boardInfo.category,
                            title = postDetail.postInfo.title,
                            content = postDetail.postInfo.content,
                            existingImageUrls = postDetail.postInfo.images
                                .sortedBy { img -> img.order }
                                .map { img -> img.url }
                        )
                    }
                    _dataState.update { WritePostDataState.Idle }
                },
                onFailure = { error ->
                    _dataState.update { WritePostDataState.Idle }
                    _eventFlow.emit(
                        WritePostEvent.SubmitError(
                            userMessage = "게시글을 불러올 수 없습니다",
                            exceptionMessage = error.message
                        )
                    )
                }
            )
        }
    }

    private fun selectAnimalType(type: String) {
        _writePostData.update { it.copy(selectedAnimalType = type) }
    }

    private fun selectTopic(topic: String) {
        _writePostData.update { it.copy(selectedTopic = topic) }
    }

    private fun updateTitle(title: String) {
        _writePostData.update { it.copy(title = title) }
    }

    private fun updateContent(content: String) {
        _writePostData.update { it.copy(content = content) }
    }

    private fun addImage(uri: Uri) {
        val maxImages = 10
        if (_writePostData.value.totalImageCount >= maxImages) return
        _writePostData.update { it.copy(newImageUris = it.newImageUris + uri) }
    }

    private fun removeImage(index: Int) {
        val data = _writePostData.value
        val existingCount = data.existingImageUrls.size
        if (index < existingCount) {
            val deletedUrl = data.existingImageUrls[index]
            _writePostData.update {
                it.copy(
                    existingImageUrls = it.existingImageUrls.toMutableList()
                        .also { list -> list.removeAt(index) },
                    deletedImageUrls = it.deletedImageUrls + deletedUrl
                )
            }
        } else {
            val newIndex = index - existingCount
            _writePostData.update {
                it.copy(
                    newImageUris = it.newImageUris.toMutableList()
                        .also { list -> list.removeAt(newIndex) }
                )
            }
        }
    }

    private fun navigateUp() {
        launch {
            _eventFlow.emit(WritePostEvent.NavigateUp)
        }
    }

    private suspend fun uploadNewImages(
        uris: List<Uri>
    ): List<String> {
        return uris.mapIndexedNotNull { index, uri ->
            val fileData = contentFileReader.readBytes(uri.toString())
                ?: return@mapIndexedNotNull null
            val bytes = fileData.bytes
            val mimeType = fileData.mimeType
            val ext = mimeType.substringAfter("/", "jpg")
            val filename = "post_${System.currentTimeMillis()}_$index.$ext"

            val presignResult = appInfoRepository.getPresignedUrl(
                listOf(PresignFileRequest(filename = filename, contentType = mimeType, usage = "POST"))
            )
            val presignedUrl = presignResult.getOrNull()?.firstOrNull()
                ?: return@mapIndexedNotNull null

            val uploadResult = uploadImageUseCase(presignedUrl.preSignedUrl, bytes, mimeType)
            if (uploadResult.isSuccess) presignedUrl.imageUrl else null
        }
    }
}
