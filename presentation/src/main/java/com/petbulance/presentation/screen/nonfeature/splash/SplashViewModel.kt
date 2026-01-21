package com.petbulance.presentation.screen.nonfeature.splash

import androidx.lifecycle.viewModelScope
import com.petbulance.domain.usecase.feature.user.auth.CheckLoginStatusUseCase
import com.petbulance.domain.usecase.feature.user.terms.GetTermsStatusUseCase
import com.petbulance.domain.usecase.nonfeature.app.CheckAppVersionUseCase
import com.petbulance.presentation.utils.BaseViewModel
import com.petbulance.presentation.utils.error.ErrorDisplayType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checkAppVersionUseCase: CheckAppVersionUseCase,
    private val checkLoginStatusUseCase: CheckLoginStatusUseCase,
    private val getTermsStatusUseCase: GetTermsStatusUseCase
) : BaseViewModel() {

    private val _event = MutableSharedFlow<SplashEvent>()
    val event: SharedFlow<SplashEvent> = _event.asSharedFlow()

    init {
        observeErrorEvent(_event)
        checkAppStatus()
    }

    private fun checkAppStatus() {
        viewModelScope.launch {
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
                            userMessage = "서버 연결에 실패했습니다.\n네트워크 상태를 확인해주세요.",
                            exceptionMessage = e.message,
                            displayType = ErrorDisplayType.Common
                        )
                    )
                }
        }
    }

    private suspend fun checkLoginAndMove() {
        val isLoggedIn = checkLoginStatusUseCase().getOrElse { false }

        if (!isLoggedIn) {
            _event.emit(SplashEvent.NavigateToLogin)
            return
        }

        getTermsStatusUseCase()
            .onSuccess { status ->
                val isAllRequiredAgreed = status.service && status.privacy && status.location
                if (isAllRequiredAgreed) {
                    _event.emit(SplashEvent.NavigateToHome)
                } else {
                    _event.emit(SplashEvent.NavigateToHomeWithTermsCheck)
                }
            }
            .onFailure {
                _event.emit(
                    SplashEvent.DataFetch.Error(
                        userMessage = "사용자 정보를 불러오는데 실패했습니다.",
                        exceptionMessage = it.message
                    )
                )
            }
    }

    fun onIntent(intent: SplashIntent) {
        /* nop */
    }
}