package com.example.domain.repository.feature.support

import com.example.domain.model.feature.support.inquiry.InquiryRequest

interface InquiryRepository {
    suspend fun createInquiry(request: InquiryRequest): Result<String>
}