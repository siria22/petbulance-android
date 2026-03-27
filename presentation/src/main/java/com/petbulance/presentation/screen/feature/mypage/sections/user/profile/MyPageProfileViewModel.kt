package com.petbulance.presentation.screen.feature.mypage.sections.user.profile

import android.net.Uri
import com.petbulance.domain.model.feature.user.user.UserInfo
import com.petbulance.domain.repository.nonfeature.app.ContentFileReader
import com.petbulance.domain.usecase.feature.user.user.GetMyInfoUseCase
import com.petbulance.domain.usecase.feature.user.user.UpdateProfileUseCase
import com.petbulance.presentation.utils.BaseViewModel
import com.petbulance.presentation.utils.error.ErrorDisplayType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MyPageProfileViewModel @Inject constructor(
    private val getMyInfoUseCase: GetMyInfoUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val contentFileReader: ContentFileReader
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<MyPageProfileDataState>(MyPageProfileDataState.Init)
    val dataState: StateFlow<MyPageProfileDataState> = _dataState

    private val _eventFlow = MutableSharedFlow<MyPageProfileEvent>()
    val eventFlow: SharedFlow<MyPageProfileEvent> = _eventFlow

    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    val userInfo: StateFlow<UserInfo?> = _userInfo

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri

    fun onIntent(intent: MyPageProfileIntent) {
        when (intent) {
            is MyPageProfileIntent.LoadUserInfo -> loadUserInfo()
            is MyPageProfileIntent.SelectImage -> _selectedImageUri.value = intent.uri
            is MyPageProfileIntent.SaveProfile -> saveProfile(
                newNickname = intent.newNickname,
                imageUriString = intent.imageUriString
            )
        }
    }

    private fun loadUserInfo() {
        launch {
            _dataState.value = MyPageProfileDataState.OnProgress
            getMyInfoUseCase()
                .onSuccess { _userInfo.value = it }
                .onFailure { e ->
                    _eventFlow.emit(
                        MyPageProfileEvent.DataFetch.Error(
                            userMessage = "사용자 정보를 불러오지 못했습니다.",
                            exceptionMessage = e.message,
                            displayType = ErrorDisplayType.Common
                        )
                    )
                }
            _dataState.value = MyPageProfileDataState.Init
        }
    }

    private fun saveProfile(newNickname: String, imageUriString: String?) {
        launch {
            _dataState.value = MyPageProfileDataState.OnProgress

            val imageData = imageUriString?.let { contentFileReader.readBytes(it) }
            val ext = imageData?.mimeType?.substringAfter("/", "jpg") ?: "jpg"

            updateProfileUseCase(
                currentNickname = _userInfo.value?.nickname ?: "",
                newNickname = newNickname,
                imageBytes = imageData?.bytes,
                imageFilename = if (imageData != null) "profile_${System.currentTimeMillis()}.$ext" else null,
                imageMimeType = imageData?.mimeType
            ).onSuccess {
                _dataState.value = MyPageProfileDataState.Init
                _eventFlow.emit(MyPageProfileEvent.SaveSuccess)
            }.onFailure { e ->
                _dataState.value = MyPageProfileDataState.Init
                _eventFlow.emit(
                    MyPageProfileEvent.DataFetch.Error(
                        userMessage = "프로필 저장에 실패했습니다.",
                        exceptionMessage = e.message,
                        displayType = ErrorDisplayType.Common
                    )
                )
            }
        }
    }

    init {
        observeErrorEvent(eventFlow)
        loadUserInfo()
    }
}
