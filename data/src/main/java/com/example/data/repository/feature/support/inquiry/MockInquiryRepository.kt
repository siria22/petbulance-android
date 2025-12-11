package com.example.data.repository.feature.support.inquiry

import com.example.domain.model.feature.support.inquiry.InquiryRequest
import com.example.domain.repository.feature.support.InquiryRepository
import jakarta.inject.Inject

class MockInquiryRepository @Inject constructor() : InquiryRepository {
    override suspend fun createInquiry(request: InquiryRequest): Result<String> {
        return Result.success("Inquiry created successfully")
    }
}