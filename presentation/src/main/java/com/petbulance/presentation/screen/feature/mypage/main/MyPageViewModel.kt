package com.petbulance.presentation.screen.feature.mypage.main

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import com.petbulance.domain.model.feature.user.user.UserInfo
import com.petbulance.domain.usecase.feature.user.user.GetMyInfoUseCase
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
class MyPageViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMyInfoUseCase: GetMyInfoUseCase,
    private val getVersionUseCase: GetVersionUseCase,
    @ApplicationContext private val context: Context
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<MyPageDataState>(MyPageDataState.Init)
    val dataState: StateFlow<MyPageDataState> = _dataState

    private val _screenState = MutableStateFlow<MyPageScreenState>(MyPageScreenState.Init)
    val screenState: StateFlow<MyPageScreenState> = _screenState

    private val _eventFlow = MutableSharedFlow<MyPageEvent>()
    val eventFlow: SharedFlow<MyPageEvent> = _eventFlow

    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    val userInfo: StateFlow<UserInfo?> = _userInfo

    private val _currentVersion = MutableStateFlow("")
    val currentVersion: StateFlow<String> = _currentVersion

    private val _latestVersion = MutableStateFlow("")
    val latestVersion: StateFlow<String> = _latestVersion

    fun onIntent(intent: MyPageIntent) {
        when (intent) {
            is MyPageIntent.SomeIntentWithoutParams -> {
                //do sth
            }

            is MyPageIntent.SomeIntentWithParams -> {
                //do sth(intent.params)
            }
        }
    }

    init {
        observeErrorEvent(eventFlow)

        launch {
            fetchUserInfo()
            fetchVersionInfo()
        }
    }

    private suspend fun fetchUserInfo() {
        _dataState.value = MyPageDataState.OnProgress

        getMyInfoUseCase()
            .onSuccess { userInfo ->
                _userInfo.value = userInfo
            }
            .onFailure { exception ->
                _eventFlow.emit(
                    MyPageEvent.DataFetch.Error(
                        displayType = ErrorDisplayType.Common,
                        userMessage = "사용자 정보를 불러오는데 실패했습니다.",
                        exceptionMessage = exception.message
                    )
                )
            }

        _dataState.value = MyPageDataState.Init
    }

    private suspend fun fetchVersionInfo() {
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            _currentVersion.value = pInfo.versionName ?: "Unknown"
        } catch (e: Exception) {
            _currentVersion.value = "Unknown"
        }

        runCatching {
            getVersionUseCase()
        }.onSuccess { version ->
            _latestVersion.value = version
        }.onFailure { exception ->
            _eventFlow.emit(
                MyPageEvent.DataFetch.Error(
                    displayType = ErrorDisplayType.Common,
                    userMessage = "앱 최신 버전을 불러오는데 실패했습니다.",
                    exceptionMessage = exception.message
                )
            )
        }
    }
}
