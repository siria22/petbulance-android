package com.example.data.datasource.remote.network.feature.support.inquiry.dto

import kotlinx.serialization.Serializable

@Serializable
data class InquiryReqDto(
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
