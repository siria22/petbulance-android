package com.petbulance.presentation.screen.nonfeature.auth

import com.petbulance.domain.usecase.feature.user.auth.CheckLoginStatusUseCase
import com.petbulance.presentation.utils.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    checkLoginStatusUseCase: CheckLoginStatusUseCase
) : BaseViewModel() {

    private val _isLoggedIn = MutableStateFlow<Boolean?>(null)
    val isLoggedIn = _isLoggedIn.asStateFlow()

    init {
        launch { _isLoggedIn.value = checkLoginStatusUseCase().getOrElse { false } }
    }
}
