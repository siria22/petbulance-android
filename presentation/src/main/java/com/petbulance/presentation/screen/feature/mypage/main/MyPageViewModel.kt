package com.petbulance.presentation.screen.feature.mypage.main

import com.petbulance.domain.model.feature.user.user.UserInfo
import com.petbulance.domain.usecase.feature.user.auth.CheckLoginStatusUseCase
import com.petbulance.domain.usecase.feature.user.user.GetMyInfoUseCase
import com.petbulance.domain.usecase.nonfeature.app.GetAppVersionUseCase
import com.petbulance.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val getMyInfoUseCase: GetMyInfoUseCase,
    private val getAppVersionUseCase: GetAppVersionUseCase,
    private val checkLoginStatusUseCase: CheckLoginStatusUseCase
) : BaseViewModel() {

    private val _eventFlow = MutableSharedFlow<MyPageEvent>()
    val eventFlow: SharedFlow<MyPageEvent> = _eventFlow

    private val _userInfo = MutableStateFlow<UserInfo?>(null)
    val userInfo: StateFlow<UserInfo?> = _userInfo

    private val _currentVersion = MutableStateFlow("")
    val currentVersion: StateFlow<String> = _currentVersion

    private val _latestVersion = MutableStateFlow("")
    val latestVersion: StateFlow<String> = _latestVersion

    init {
        observeErrorEvent(eventFlow)
        launch { fetchUserInfo() }
        launch { fetchVersionInfo() }
    }

    private suspend fun fetchUserInfo() {
        val isLoggedIn = checkLoginStatusUseCase().getOrElse { false }
        if (!isLoggedIn) return

        getMyInfoUseCase()
            .onSuccess { userInfo ->
                _userInfo.value = userInfo
            }
            .onFailure { exception ->
                if (exception.message?.contains("ACCOUNT_SUSPENDED") == true) {
                    _eventFlow.emit(MyPageEvent.AccountSuspended)
                }
            }
    }

    private suspend fun fetchVersionInfo() {
        getAppVersionUseCase.getCurrentVersion()
            .onSuccess { version -> _currentVersion.value = version }
            .onFailure { _currentVersion.value = "Unknown" }

        // TODO: 앱 버전 관리 방식 결정 필요
        //  - Google Play In-App Updates API (com.google.android.play:app-update)로 최신 버전 확인 가능
        //  - AppUpdateManager.appUpdateInfo에서 availableVersionCode 조회
        //  - 서버 API 없이도 Play Store 기준 업데이트 유도 가능
        //  - 현재는 서버 API가 더미 데이터를 반환하므로, Play Store 배포 후 In-App Updates 전환 검토
        getAppVersionUseCase.getLatestVersion()
            .onSuccess { version -> _latestVersion.value = version }
            .onFailure { _latestVersion.value = "" }
    }
}
