package com.petbulance.presentation.screen.feature.mypage.sections.user.withdrawal

import com.petbulance.domain.usecase.feature.user.user.DeleteAccountUseCase
import com.petbulance.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class WithdrawalViewModel @Inject constructor(
    private val deleteAccountUseCase: DeleteAccountUseCase,
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<WithdrawalDataState>(WithdrawalDataState.Init)
    val dataState: StateFlow<WithdrawalDataState> = _dataState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<WithdrawalEvent>()
    val eventFlow: SharedFlow<WithdrawalEvent> = _eventFlow

    init {
        observeErrorEvent(eventFlow)
    }

    fun onIntent(intent: WithdrawalIntent) {
        if (_dataState.value == WithdrawalDataState.OnProgress) return
        when (intent) {
            is WithdrawalIntent.ConfirmWithdrawal -> deleteAccount()
        }
    }

    private fun deleteAccount() {
        launch {
            _dataState.value = WithdrawalDataState.OnProgress
            deleteAccountUseCase()
                .onSuccess {
                    _eventFlow.emit(WithdrawalEvent.WithdrawalSuccess)
                }
                .onFailure { e ->
                    _eventFlow.emit(
                        WithdrawalEvent.WithdrawalFailed(exceptionMessage = e.message)
                    )
                }
            _dataState.value = WithdrawalDataState.Init
        }
    }
}
