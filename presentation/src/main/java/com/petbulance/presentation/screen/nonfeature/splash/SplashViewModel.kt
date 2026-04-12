package com.petbulance.presentation.screen.nonfeature.splash

import com.petbulance.domain.repository.feature.user.AuthRepository
import com.petbulance.domain.usecase.feature.user.auth.CheckLoginStatusUseCase
import com.petbulance.domain.usecase.feature.user.terms.GetTermsListUseCase
import com.petbulance.domain.usecase.feature.user.terms.GetTermsStatusUseCase
import com.petbulance.domain.usecase.nonfeature.app.CheckAppVersionUseCase
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
    private val getTermsListUseCase: GetTermsListUseCase,
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
            checkAppVersionUseCase()
                .onSuccess { isUpdateNeeded ->
                    if (isUpdateNeeded) {
                        // TODO: 업데이트 필요 시 처리 (강제 업데이트 다이얼로그 등)
                    }
                    checkLoginAndMove()
                }
                .onFailure { e ->
                    _event.emit(
                        SplashEvent.DataFetch.Error(
                            userMessage = "네트워크 연결을 확인해주세요",
                            exceptionMessage = e.message,
                            displayType = ErrorDisplayType.Common
                        )
                    )
                }
        }
    }

    private suspend fun checkLoginAndMove() {
        val isAutoLoginEnabled = authRepository.isAutoLoginEnabled().getOrElse { true }
        val hasTokens = checkLoginStatusUseCase().getOrElse { false }

        if (!isAutoLoginEnabled || !hasTokens) {
            if (!isAutoLoginEnabled && hasTokens) {
                authRepository.clearTokens()
            }
            _event.emit(SplashEvent.NavigateToLogin)
            return
        }

        // 약관 목록과 동의 상태를 함께 확인
        val termsListResult = getTermsListUseCase()
        val termsStatusResult = getTermsStatusUseCase()

        if (termsListResult.isFailure || termsStatusResult.isFailure) {
            _event.emit(
                SplashEvent.DataFetch.Error(
                    userMessage = "서버와의 통신이 원활하지 않습니다.",
                    exceptionMessage = termsListResult.exceptionOrNull()?.message 
                        ?: termsStatusResult.exceptionOrNull()?.message,
                    displayType = ErrorDisplayType.Custom
                )
            )
            return
        }

        val termsList = termsListResult.getOrNull() ?: emptyList()
        val termsStatus = termsStatusResult.getOrNull() ?: return

        // 필수 약관 목록 추출
        val requiredTerms = termsList.filter { it.required }
        
        // 각 필수 약관이 동의되었는지 확인
        val isAllRequiredAgreed = requiredTerms.all { term ->
            when (term.termsType?.name) {
                "SERVICE" -> termsStatus.service
                "PRIVACY" -> termsStatus.privacy
                "LOCATION" -> termsStatus.location
                "MARKETING" -> termsStatus.marketing
                else -> false
            }
        }

        if (isAllRequiredAgreed) {
            _event.emit(SplashEvent.NavigateToHome)
        } else {
            _event.emit(SplashEvent.NavigateToHomeWithTermsCheck)
        }
    }

    fun onIntent(intent: SplashIntent) {
        /* nop */
    }
}