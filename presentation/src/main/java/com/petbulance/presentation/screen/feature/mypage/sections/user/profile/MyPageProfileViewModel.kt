package com.petbulance.presentation.screen.feature.mypage.sections.user.profile

import android.content.Context
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import com.petbulance.domain.model.feature.user.user.UserInfo
import com.petbulance.domain.usecase.feature.user.user.GetMyInfoUseCase
import com.petbulance.domain.usecase.feature.user.user.UpdateProfileUseCase
import com.petbulance.presentation.utils.BaseViewModel
import com.petbulance.presentation.utils.error.ErrorDisplayType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MyPageProfileViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context,
    private val getMyInfoUseCase: GetMyInfoUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<MyPageProfileDataState>(MyPageProfileDataState.Init)
    val dataState: StateFlow<MyPageProfileDataState> = _dataState

    private val _screenState = MutableStateFlow<MyPageProfileScreenState>(MyPageProfileScreenState.Init)
    val screenState: StateFlow<MyPageProfileScreenState> = _screenState

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
                imageBytes = intent.imageBytes,
                imageFilename = intent.imageFilename,
                imageMimeType = intent.imageMimeType
            )
            is MyPageProfileIntent.UpdateNickname -> { /* 닉네임 입력 상태는 Screen에서 관리 */ }
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

    private fun saveProfile(
        newNickname: String,
        imageBytes: ByteArray?,
        imageFilename: String?,
        imageMimeType: String?
    ) {
        launch {
            _dataState.value = MyPageProfileDataState.OnProgress
            updateProfileUseCase(
                currentNickname = _userInfo.value?.nickname ?: "",
                newNickname = newNickname,
                imageBytes = imageBytes,
                imageFilename = imageFilename,
                imageMimeType = imageMimeType
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