package com.petbulance.presentation.screen.feature.mypage.sections.user.account

import MyPageAccountData
import com.petbulance.domain.model.type.LoginProviderType
import com.petbulance.domain.usecase.feature.user.auth.GetAutoLoginEnabledUseCase
import com.petbulance.domain.usecase.feature.user.auth.SetAutoLoginEnabledUseCase
import com.petbulance.domain.usecase.feature.user.user.ConnectSocialAccountUseCase
import com.petbulance.domain.usecase.feature.user.user.DisconnectSocialAccountUseCase
import com.petbulance.domain.usecase.feature.user.user.GetMyInfoUseCase
import com.petbulance.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MyPageAccountViewModel @Inject constructor(
    private val getMyInfoUseCase: GetMyInfoUseCase,
    private val getAutoLoginEnabledUseCase: GetAutoLoginEnabledUseCase,
    private val setAutoLoginEnabledUseCase: SetAutoLoginEnabledUseCase,
    private val connectSocialAccountUseCase: ConnectSocialAccountUseCase,
    private val disconnectSocialAccountUseCase: DisconnectSocialAccountUseCase,
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<MyPageAccountDataState>(MyPageAccountDataState.Init)
    val dataState: StateFlow<MyPageAccountDataState> = _dataState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<MyPageAccountEvent>()
    val eventFlow: SharedFlow<MyPageAccountEvent> = _eventFlow

    private val _uiState = MutableStateFlow(MyPageAccountData.empty)
    val uiState: StateFlow<MyPageAccountData> = _uiState.asStateFlow()

    fun onIntent(intent: MyPageAccountIntent) {
        if (_dataState.value == MyPageAccountDataState.OnProgress) return

        when (intent) {
            is MyPageAccountIntent.ToggleAutoLogin -> toggleAutoLogin(intent.isEnabled)
            is MyPageAccountIntent.ConnectSocial -> connectSocial(intent.provider, intent.token)
            is MyPageAccountIntent.DisconnectSocial -> disconnectSocial(intent.provider)
        }
    }

    init {
        observeErrorEvent(eventFlow)
        fetchData()
    }

    private fun fetchData() {
        launch {
            _dataState.value = MyPageAccountDataState.OnProgress
            loadAccountData()
            _dataState.value = MyPageAccountDataState.Init
        }
    }

    private suspend fun loadAccountData() {
        val userResult = getMyInfoUseCase()
        val autoLoginResult = getAutoLoginEnabledUseCase()

        userResult.onSuccess { userInfo ->
            _uiState.update {
                it.copy(
                    userInfo = userInfo,
                    isAutoLoginEnabled = autoLoginResult.getOrElse { true }
                )
            }
        }
    }

    private fun toggleAutoLogin(enabled: Boolean) {
        launch {
            setAutoLoginEnabledUseCase(enabled)
                .onSuccess {
                    _uiState.update { it.copy(isAutoLoginEnabled = enabled) }
                }
        }
    }

    private fun connectSocial(provider: LoginProviderType, token: String) {
        launch {
            _dataState.value = MyPageAccountDataState.OnProgress
            connectSocialAccountUseCase(provider.name, token)
                .onSuccess {
                    loadAccountData()
                }
                .onFailure { e ->
                    _eventFlow.emit(MyPageAccountEvent.DataFetch.Error(exceptionMessage = e.message))
                }
            _dataState.value = MyPageAccountDataState.Init
        }
    }

    private fun disconnectSocial(provider: LoginProviderType) {
        val currentInfo = _uiState.value.userInfo ?: return
        val socials = currentInfo.connectedSocials

        val isTargetConnected = when (provider) {
            LoginProviderType.KAKAO -> socials.kakao != null
            LoginProviderType.GOOGLE -> socials.google != null
            LoginProviderType.NAVER -> socials.naver != null
        }
        if (!isTargetConnected) return

        val connectedCount = listOfNotNull(socials.kakao, socials.google, socials.naver).count()

        if (connectedCount <= 1) {
            launch { _eventFlow.emit(MyPageAccountEvent.SocialLink.IsLast) }
            return
        }

        launch {
            _dataState.value = MyPageAccountDataState.OnProgress
            disconnectSocialAccountUseCase(provider.name)
                .onSuccess {
                    loadAccountData()
                }
                .onFailure { e ->
                    _eventFlow.emit(MyPageAccountEvent.SocialLink.DisconnectFailed(exceptionMessage = e.message))
                }
            _dataState.value = MyPageAccountDataState.Init
        }
    }
}
