package com.example.presentation.screen.nonfeature.login.main

import com.example.domain.model.type.LoginProviderType
import com.example.domain.usecase.feature.user.auth.GetLastLoginPlatformUseCase
import com.example.domain.usecase.feature.user.auth.SocialLoginUseCase
import com.example.presentation.utils.BaseViewModel
import com.example.presentation.utils.error.ErrorDisplayType
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
    private val getLastLoginPlatformUseCase: GetLastLoginPlatformUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<LoginDataState>(LoginDataState.Init)
    val dataState: StateFlow<LoginDataState> = _dataState.asStateFlow()

    private val _screenState = MutableStateFlow<LoginScreenState>(LoginScreenState.Init)
    val screenState: StateFlow<LoginScreenState> = _screenState.asStateFlow()

    private val _event = MutableSharedFlow<LoginEvent>()
    val event: SharedFlow<LoginEvent> = _event.asSharedFlow()

    private val _lastLoginPlatform = MutableStateFlow<LoginProviderType?>(null)
    val lastLoginPlatform: StateFlow<LoginProviderType?> = _lastLoginPlatform.asStateFlow()

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
                    _event.emit(LoginEvent.NavigateToTerms)
                } else {
                    _event.emit(LoginEvent.NavigateToHome)
                }
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
        _dataState.value = LoginDataState.Loading
        runCatching {
            getLastLoginPlatformUseCase()
        }.onSuccess { result ->
            _lastLoginPlatform.value = result.getOrThrow()
        }.onFailure { ex ->
            _event.emit(
                LoginEvent.DataFetch.Error(
                    userMessage = "error messages",
                    exceptionMessage = ex.message
                )
            )
        }
        _dataState.value = LoginDataState.Init
    }
}