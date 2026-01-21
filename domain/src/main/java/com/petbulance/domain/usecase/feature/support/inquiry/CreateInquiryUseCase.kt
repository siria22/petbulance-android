package com.petbulance.domain.usecase.feature.support.inquiry

import com.petbulance.domain.model.feature.support.inquiry.InquiryRequest
import com.petbulance.domain.repository.feature.support.InquiryRepository
import javax.inject.Inject

class CreateInquiryUseCase @Inject constructor(
    private val repository: InquiryRepository
) {
    suspend operator fun invoke(inquiry: InquiryRequest): String {
        return repository.createInquiry(request = inquiry).getOrThrow()
    }
}