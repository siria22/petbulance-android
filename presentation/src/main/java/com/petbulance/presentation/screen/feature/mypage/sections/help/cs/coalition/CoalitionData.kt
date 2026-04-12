package com.petbulance.presentation.screen.feature.mypage.sections.help.cs.coalition

data class CoalitionData(
    val inquiryType: String,
    val companyName: String,
    val managerName: String,
    val managerPosition: String,
    val phone: String,
    val email: String,
    val interestTypes: List<String>,
    val content: String,
    val privacyConsent: Boolean,
    val isSubmitEnabled: Boolean
) {
    companion object {
        fun empty() = CoalitionData(
            inquiryType = "",
            companyName = "",
            managerName = "",
            managerPosition = "",
            phone = "",
            email = "",
            interestTypes = emptyList(),
            content = "",
            privacyConsent = false,
            isSubmitEnabled = false
        )

        fun stub() = CoalitionData(
            inquiryType = "광고 문의",
            companyName = "펫블런스 동물병원",
            managerName = "김펫블",
            managerPosition = "마케팅 매니저",
            phone = "010-1234-5678",
            email = "petbulance@company.com",
            interestTypes = listOf("배너 광고", "병원 등록"),
            content = "홈 상단에 배너광고 문의합니다. 배너는 병원 전문 안내 및 이벤트 관련이고,",
            privacyConsent = true,
            isSubmitEnabled = true
        )
    }
}