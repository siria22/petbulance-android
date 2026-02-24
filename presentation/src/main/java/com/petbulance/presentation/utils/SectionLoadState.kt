package com.petbulance.presentation.utils

/**
 * 섹션별 데이터 로드 상태를 정의하는 공통 클래스
 */
sealed class SectionLoadState {
    data object Init : SectionLoadState()
    data object Loading : SectionLoadState()
    data object Success : SectionLoadState()
    data class Error(val message: String) : SectionLoadState()
}
