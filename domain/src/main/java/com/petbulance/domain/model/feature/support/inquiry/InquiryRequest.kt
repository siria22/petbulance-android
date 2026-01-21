package com.petbulance.domain.model.feature.support.inquiry

data class InquiryRequest(
    val type: String,
    val companyName: String,
    val managerName: String,
    val managerPosition: String,
    val phone: String,
    val email: String,
    val interestType: String,
    val content: String,
    val privacyConsent: Boolean
)