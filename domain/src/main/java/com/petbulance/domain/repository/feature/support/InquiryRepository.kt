package com.petbulance.domain.repository.feature.support

import com.petbulance.domain.model.feature.support.inquiry.InquiryRequest

interface InquiryRepository {
    suspend fun createInquiry(request: InquiryRequest): Result<String>
}