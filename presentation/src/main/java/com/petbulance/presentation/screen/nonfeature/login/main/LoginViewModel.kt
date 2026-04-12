package com.petbulance.presentation.screen.nonfeature.login.main

import com.petbulance.domain.model.type.LoginProviderType
import com.petbulance.domain.usecase.feature.user.auth.GetLastLoginPlatformUseCase
import com.petbulance.domain.usecase.feature.user.auth.SocialLoginUseCase
import com.petbulance.presentation.analytics.AnalyticsEvents
import com.petbulance.presentation.analytics.AnalyticsTracker
import com.petbulance.presentation.utils.BaseViewModel
import com.petbulance.presentation.utils.error.ErrorDisplayType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val socialLoginUseCase: SocialLoginUseCase,
    private val getLastLoginPlatformUseCase: GetLastLoginPlatformUseCase,
    private val analyticsTracker: AnalyticsTracker
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<LoginDataState>(LoginDataState.Init)
    val dataState: StateFlow<LoginDataState> = _dataState.asStateFlow()

    private val _screenState = MutableStateFlow(LoginScreenState())
    val screenState: StateFlow<LoginScreenState> = _screenState.asStateFlow()

    private val _event = MutableSharedFlow<LoginEvent>()
    val event: SharedFlow<LoginEvent> = _event.asSharedFlow()

    init {
        observeErrorEvent(_event)
        launch {
            getLastLoginPlatform()
        }
    }

    fun onIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.OnSocialLogin -> {
                launch {
                    login(intent.provider, intent.token)
                }
            }
        }
    }

    private suspend fun login(provider: LoginProviderType, token: String) {
        _dataState.value = LoginDataState.Loading

        socialLoginUseCase(provider, token)
            .onSuccess { result ->
                _dataState.value = LoginDataState.Init
                if (result.isNewUser) {
                    // GA4: sign_up_complete
                    analyticsTracker.trackEvent(
                        AnalyticsEvents.SIGN_UP_COMPLETE,
                        mapOf(AnalyticsEvents.Params.SIGNUP_METHOD to provider.korean)
                    )
                    _event.emit(LoginEvent.NavigateToTerms)
                } else {
                    _event.emit(LoginEvent.NavigateToHome)
                }
                analyticsTracker.setUserProperty("login_method", provider.korean)
            }
            .onFailure { e ->
                _dataState.value = LoginDataState.Init
                _event.emit(
                    LoginEvent.DataFetch.Error(
                        userMessage = "로그인에 실패했습니다.",
                        exceptionMessage = e.message,
                        displayType = ErrorDisplayType.Common
                    )
                )
            }
    }

    private suspend fun getLastLoginPlatform() {
        runCatching {
            getLastLoginPlatformUseCase()
        }.onSuccess { result ->
            val platform = result.getOrNull()
            _screenState.value = _screenState.value.copy(lastLoginPlatform = platform)
        }.onFailure {
            // 최근 로그인 플랫폼 로딩 실패는 로그인 플로우에 영향 없음
            // 단순히 UI에 표시하지 않음
        }
    }
}