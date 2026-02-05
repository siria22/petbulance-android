package com.petbulance.presentation.screen.nonfeature.splash

import android.util.Log
import com.petbulance.domain.repository.feature.user.AuthRepository
import com.petbulance.domain.usecase.feature.user.auth.CheckLoginStatusUseCase
import com.petbulance.domain.usecase.feature.user.terms.GetTermsStatusUseCase
import com.petbulance.domain.usecase.nonfeature.app.CheckAppVersionUseCase
import com.petbulance.domain.utils.LOGGER_TAG
import com.petbulance.presentation.utils.BaseViewModel
import com.petbulance.presentation.utils.error.ErrorDisplayType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkAppVersionUseCase: CheckAppVersionUseCase,
    private val checkLoginStatusUseCase: CheckLoginStatusUseCase,
    private val getTermsStatusUseCase: GetTermsStatusUseCase,
    private val authRepository: AuthRepository
) : BaseViewModel() {

    private val _event = MutableSharedFlow<SplashEvent>()
    val event: SharedFlow<SplashEvent> = _event.asSharedFlow()

    init {
        observeErrorEvent(_event)
        checkAppStatus()
    }

    private fun checkAppStatus() {
        launch {
            Log.d(LOGGER_TAG, "Check app status")
            checkAppVersionUseCase()
                .onSuccess { isUpdateNeeded ->
                    if (isUpdateNeeded) {
                        // TODO: 업데이트 필요 시 처리 (강제 업데이트 다이얼로그 등)
                    }
                    Log.d(LOGGER_TAG, "App version check success: $isUpdateNeeded")
                    checkLoginAndMove()
                }
                .onFailure { e ->
                    _event.emit(
                        SplashEvent.DataFetch.Error(
                            userMessage = "서버 연결에 실패했습니다.\n네트워크 상태를 확인해주세요.",
                            exceptionMessage = e.message,
                            displayType = ErrorDisplayType.Common
                        )
                    )
                }
        }
    }

    private suspend fun checkLoginAndMove() {
        // 자동 로그인 설정 확인
        val isAutoLoginEnabled = authRepository.isAutoLoginEnabled().getOrElse { true }

        // 토큰 존재 여부 확인
        val hasTokens = checkLoginStatusUseCase().getOrElse { false }

        if (!isAutoLoginEnabled || !hasTokens) {
            Log.d(LOGGER_TAG, "User is not logged in or AutoLogin disabled")
            if (!isAutoLoginEnabled && hasTokens) {
                authRepository.clearTokens()
            }
            _event.emit(SplashEvent.NavigateToLogin)
            return
        }

        Log.d(LOGGER_TAG, "User is logged in")

        getTermsStatusUseCase()
            .onSuccess { status ->
                val isAllRequiredAgreed = status.service && status.privacy && status.location
                Log.d(LOGGER_TAG, "Is All Required terms Agreed: $isAllRequiredAgreed")
                if (isAllRequiredAgreed) {
                    Log.d(LOGGER_TAG, "Navigate to home")
                    _event.emit(SplashEvent.NavigateToHome)
                } else {
                    Log.d(LOGGER_TAG, "Navigate to Home with Terms check")
                    _event.emit(SplashEvent.NavigateToHomeWithTermsCheck)
                }
            }
            .onFailure { ex ->
                Log.d(LOGGER_TAG, "Failed to get Terms status: ${ex.stackTrace}")
                _event.emit(
                    SplashEvent.DataFetch.Error(
                        userMessage = "서버와의 통신이 원활하지 않습니다.",
                        exceptionMessage = ex.message,
                        displayType = ErrorDisplayType.Custom
                    )
                )
            }
    }

    fun onIntent(intent: SplashIntent) {
        /* nop */
    }
}