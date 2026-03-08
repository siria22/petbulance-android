package com.petbulance.presentation.screen.feature.mypage.sections.help.cs.coalition

import androidx.lifecycle.SavedStateHandle
import com.petbulance.domain.model.feature.support.inquiry.InquiryRequest
import com.petbulance.domain.usecase.feature.support.inquiry.CreateInquiryUseCase
import com.petbulance.presentation.utils.BaseViewModel
import com.petbulance.presentation.utils.error.ErrorDisplayType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class CoalitionViewModel @Inject constructor(
    private val createInquiryUseCase: CreateInquiryUseCase
) : BaseViewModel() {

    private val _dataState = MutableStateFlow<CoalitionDataState>(CoalitionDataState.Init)
    val dataState: StateFlow<CoalitionDataState> = _dataState

    private val _screenState = MutableStateFlow<CoalitionScreenState>(CoalitionScreenState.Init)
    val screenState: StateFlow<CoalitionScreenState> = _screenState

    private val _eventFlow = MutableSharedFlow<CoalitionEvent>()
    val eventFlow: SharedFlow<CoalitionEvent> = _eventFlow

    // 문의 유형
    private val _inquiryType = MutableStateFlow("")
    val inquiryType: StateFlow<String> = _inquiryType

    // 회사/병원명
    private val _companyName = MutableStateFlow("")
    val companyName: StateFlow<String> = _companyName

    // 담당자명
    private val _managerName = MutableStateFlow("")
    val managerName: StateFlow<String> = _managerName

    // 담당자 직책
    private val _managerPosition = MutableStateFlow("")
    val managerPosition: StateFlow<String> = _managerPosition

    // 연락처
    private val _phone = MutableStateFlow("")
    val phone: StateFlow<String> = _phone

    // 이메일
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    // 관심 항목 (중복 선택 가능)
    private val _interestTypes = MutableStateFlow<List<String>>(emptyList())
    val interestTypes: StateFlow<List<String>> = _interestTypes

    // 문의 내용
    private val _content = MutableStateFlow("")
    val content: StateFlow<String> = _content

    // 개인정보 동의
    private val _privacyConsent = MutableStateFlow(false)
    val privacyConsent: StateFlow<Boolean> = _privacyConsent

    // 제출 버튼 활성화 여부
    private val _isSubmitEnabled = MutableStateFlow(false)
    val isSubmitEnabled: StateFlow<Boolean> = _isSubmitEnabled

    fun onIntent(intent: CoalitionIntent) {
        when (intent) {
            is CoalitionIntent.OnInquiryTypeChanged -> {
                _inquiryType.value = intent.type
                updateSubmitButtonState()
            }

            is CoalitionIntent.OnCompanyNameChanged -> {
                _companyName.value = intent.name.take(100)
                updateSubmitButtonState()
            }

            is CoalitionIntent.OnManagerNameChanged -> {
                _managerName.value = intent.name.take(50)
                updateSubmitButtonState()
            }

            is CoalitionIntent.OnManagerPositionChanged -> {
                _managerPosition.value = intent.position.take(50)
                updateSubmitButtonState()
            }

            is CoalitionIntent.OnPhoneChanged -> {
                // 숫자만 입력 허용, 하이픈 자동 삽입
                val digitsOnly = intent.phone.filter { it.isDigit() }.take(11)
                _phone.value = formatPhoneNumber(digitsOnly)
                updateSubmitButtonState()
            }

            is CoalitionIntent.OnEmailChanged -> {
                _email.value = intent.email.take(100)
                updateSubmitButtonState()
            }

            is CoalitionIntent.OnInterestTypeToggled -> {
                val currentList = _interestTypes.value.toMutableList()
                if (currentList.contains(intent.type)) {
                    currentList.remove(intent.type)
                } else {
                    currentList.add(intent.type)
                }
                _interestTypes.value = currentList
                updateSubmitButtonState()
            }

            is CoalitionIntent.OnContentChanged -> {
                _content.value = intent.content.take(1000)
                updateSubmitButtonState()
            }

            is CoalitionIntent.OnPrivacyConsentChanged -> {
                _privacyConsent.value = intent.consent
                updateSubmitButtonState()
            }

            is CoalitionIntent.OnSubmitClicked -> {
                if (_isSubmitEnabled.value) {
                    submitInquiry()
                }
            }
        }
    }

    init {
        observeErrorEvent(eventFlow)
    }

    /**
     * 전화번호 포맷팅 (010-1234-5678)
     */
    private fun formatPhoneNumber(digits: String): String {
        return when (digits.length) {
            in 0..3 -> digits
            in 4..7 -> "${digits.take(3)}-${digits.substring(3)}"
            in 8..11 -> "${digits.take(3)}-${digits.substring(3, 7)}-${digits.substring(7)}"
            else -> digits
        }
    }

    /**
     * 이메일 형식 검증
     */
    private fun isValidEmail(email: String): Boolean {
        return email.contains("@") && email.contains(".") && email.length >= 5
    }

    /**
     * 전화번호 검증 (숫자만 추출하여 10-11자리 확인)
     */
    private fun isValidPhone(phone: String): Boolean {
        val digitsOnly = phone.filter { it.isDigit() }
        return digitsOnly.length in 10..11
    }

    /**
     * 제출 버튼 활성화 상태 업데이트
     * 모든 필수 필드가 입력되고, 형식이 올바른 경우에만 활성화
     */
    private fun updateSubmitButtonState() {
        _isSubmitEnabled.value = _inquiryType.value.isNotBlank() &&
                _companyName.value.isNotBlank() &&
                _managerName.value.isNotBlank() &&
                _managerPosition.value.isNotBlank() &&
                isValidPhone(_phone.value) &&
                isValidEmail(_email.value) &&
                _interestTypes.value.isNotEmpty() &&
                _content.value.isNotBlank() &&
                _privacyConsent.value
    }

    /**
     * 제휴 문의 제출
     */
    private fun submitInquiry() {
        launch {
            _dataState.value = CoalitionDataState.OnProgress

            // 전화번호에서 하이픈 제거 (서버로는 숫자만 전송)
            val phoneDigitsOnly = _phone.value.filter { it.isDigit() }

            val request = InquiryRequest(
                type = _inquiryType.value.trim(),
                companyName = _companyName.value.trim(),
                managerName = _managerName.value.trim(),
                managerPosition = _managerPosition.value.trim(),
                phone = phoneDigitsOnly,
                email = _email.value.trim(),
                interestType = _interestTypes.value.joinToString(", "),
                content = _content.value.trim(),
                privacyConsent = _privacyConsent.value
            )

            runCatching {
                createInquiryUseCase(request)
            }.onSuccess { message ->
                _eventFlow.emit(CoalitionEvent.SubmitSuccess(message))
            }.onFailure { exception ->
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
}
