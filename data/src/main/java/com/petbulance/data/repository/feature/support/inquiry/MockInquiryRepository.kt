package com.petbulance.data.repository.feature.support.inquiry

import com.petbulance.domain.model.feature.support.inquiry.InquiryRequest
import com.petbulance.domain.repository.feature.support.InquiryRepository
import jakarta.inject.Inject

class MockInquiryRepository @Inject constructor() : InquiryRepository {
    override suspend fun createInquiry(request: InquiryRequest): Result<String> {
        return Result.success("Inquiry created successfully")
    }
}