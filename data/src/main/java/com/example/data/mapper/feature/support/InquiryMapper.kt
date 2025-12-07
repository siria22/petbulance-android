package com.example.data.mapper.feature.support

import com.example.data.datasource.remote.network.feature.support.inquiry.dto.InquiryReqDto
import com.example.domain.model.feature.support.inquiry.InquiryRequest

fun InquiryRequest.toDto() = InquiryReqDto(
    type = type,
    companyName = companyName,
    managerName = managerName,
    managerPosition = managerPosition,
    phone = phone,
    email = email,
    interestType = interestType,
    content = content,
    privacyConsent = privacyConsent
)