package com.petbulance.presentation.screen.feature.mypage.sections.help.cs.coalition

import com.petbulance.domain.model.feature.support.inquiry.InquiryRequest
import com.petbulance.domain.usecase.feature.support.inquiry.CreateInquiryUseCase
import com.petbulance.presentation.utils.BaseViewModel
import com.petbulance.presentation.utils.error.ErrorDisplayType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CoalitionViewModel @Inject constructor(
    private val createInquiryUseCase: CreateInquiryUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<CoalitionDataState>(CoalitionDataState.Init)
    val dataState: StateFlow<CoalitionDataState> = _dataState

    private val _eventFlow = MutableSharedFlow<CoalitionEvent>()
    val eventFlow: SharedFlow<CoalitionEvent> = _eventFlow

    private val _formState = MutableStateFlow(CoalitionData.empty())
    val formState: StateFlow<CoalitionData> = _formState

    fun onIntent(intent: CoalitionIntent) {
        when (intent) {
            is CoalitionIntent.OnInquiryTypeChanged -> updateForm { copy(inquiryType = intent.type) }
            is CoalitionIntent.OnCompanyNameChanged -> updateForm { copy(companyName = intent.name.take(MAX_COMPANY_NAME_LENGTH)) }
            is CoalitionIntent.OnManagerNameChanged -> updateForm { copy(managerName = intent.name.take(MAX_NAME_LENGTH)) }
            is CoalitionIntent.OnManagerPositionChanged -> updateForm { copy(managerPosition = intent.position.take(MAX_NAME_LENGTH)) }
            is CoalitionIntent.OnPhoneChanged -> {
                val digitsOnly = intent.phone.filter { it.isDigit() }.take(MAX_PHONE_DIGITS)
                updateForm { copy(phone = formatPhoneNumber(digitsOnly)) }
            }
            is CoalitionIntent.OnEmailChanged -> updateForm { copy(email = intent.email.take(MAX_EMAIL_LENGTH)) }
            is CoalitionIntent.OnInterestTypeToggled -> {
                _formState.update { current ->
                    val updated = current.interestTypes.toMutableList().apply {
                        if (contains(intent.type)) remove(intent.type) else add(intent.type)
                    }
                    current.copy(interestTypes = updated)
                }
                refreshSubmitEnabled()
            }
            is CoalitionIntent.OnContentChanged -> updateForm { copy(content = intent.content.take(MAX_CONTENT_LENGTH)) }
            is CoalitionIntent.OnPrivacyConsentChanged -> updateForm { copy(privacyConsent = intent.consent) }
            is CoalitionIntent.OnSubmitClicked -> {
                if (_formState.value.isSubmitEnabled) submitInquiry()
            }
        }
    }

    init {
        observeErrorEvent(eventFlow)
    }

    private inline fun updateForm(crossinline transform: CoalitionData.() -> CoalitionData) {
        _formState.update { it.transform() }
        refreshSubmitEnabled()
    }

    private fun refreshSubmitEnabled() {
        _formState.update { current ->
            current.copy(
                isSubmitEnabled = current.inquiryType.isNotBlank() &&
                    current.companyName.isNotBlank() &&
                    current.managerName.isNotBlank() &&
                    current.managerPosition.isNotBlank() &&
                    isValidPhone(current.phone) &&
                    isValidEmail(current.email) &&
                    current.interestTypes.isNotEmpty() &&
                    current.content.isNotBlank() &&
                    current.privacyConsent
            )
        }
    }

    private fun formatPhoneNumber(digits: String): String {
        return when (digits.length) {
            in 0..3 -> digits
            in 4..7 -> "${digits.take(3)}-${digits.substring(3)}"
            in 8..11 -> "${digits.take(3)}-${digits.substring(3, 7)}-${digits.substring(7)}"
            else -> digits
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".") && email.length >= MIN_EMAIL_LENGTH
    }

    private fun isValidPhone(phone: String): Boolean {
        val digitsOnly = phone.filter { it.isDigit() }
        return digitsOnly.length in MIN_PHONE_DIGITS..MAX_PHONE_DIGITS
    }

    private fun submitInquiry() {
        launch {
            _dataState.value = CoalitionDataState.OnProgress

            val form = _formState.value
            val phoneDigitsOnly = form.phone.filter { it.isDigit() }

            val request = InquiryRequest(
                type = form.inquiryType.trim(),
                companyName = form.companyName.trim(),
                managerName = form.managerName.trim(),
                managerPosition = form.managerPosition.trim(),
                phone = phoneDigitsOnly,
                email = form.email.trim(),
                interestType = form.interestTypes.joinToString(", "),
                content = form.content.trim(),
                privacyConsent = form.privacyConsent
            )

            createInquiryUseCase(request)
                .onSuccess { message ->
                    _eventFlow.emit(CoalitionEvent.SubmitSuccess(message))
                }
                .onFailure { exception ->
                    _eventFlow.emit(
                        CoalitionEvent.Submit.Error(
                            displayType = ErrorDisplayType.Common,
                            userMessage = "문의 제출에 실패했습니다. 다시 시도해주세요.",
                            exceptionMessage = exception.message
                        )
                    )
                }

            _dataState.value = CoalitionDataState.Init
        }
    }

    companion object {
        private const val MAX_COMPANY_NAME_LENGTH = 100
        private const val MAX_NAME_LENGTH = 50
        private const val MAX_EMAIL_LENGTH = 100
        private const val MAX_CONTENT_LENGTH = 1000
        private const val MAX_PHONE_DIGITS = 11
        private const val MIN_PHONE_DIGITS = 10
        private const val MIN_EMAIL_LENGTH = 5
    }
}
