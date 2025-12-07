package com.example.domain.usecase.feature.inquiry

import com.example.domain.model.feature.support.inquiry.InquiryRequest
import com.example.domain.repository.feature.support.InquiryRepository
import javax.inject.Inject

class CreateInquiryUseCase @Inject constructor(
    private val repository: InquiryRepository
) {
    suspend operator fun invoke(inquiry: InquiryRequest): String {
        return repository.createInquiry(request = inquiry).getOrThrow()
    }
}