package com.petbulance.data.repository.feature.support.inquiry

import com.petbulance.data.datasource.remote.network.feature.support.inquiry.InquiryApi
import com.petbulance.data.datasource.remote.network.feature.support.inquiry.dto.InquiryResDto
import com.petbulance.data.datasource.remote.network.common.safeApiCall
import com.petbulance.data.mapper.feature.support.toDto
import com.petbulance.domain.model.feature.support.inquiry.InquiryRequest
import com.petbulance.domain.repository.feature.support.InquiryRepository
import javax.inject.Inject

class InquiryRepositoryImpl @Inject constructor(
    private val api: InquiryApi
) : InquiryRepository {

    override suspend fun createInquiry(request: InquiryRequest): Result<String> {
        return safeApiCall<InquiryResDto>(path = "/inquiries")
        { api.createInquiry(request.toDto()) }
            .map { it.message }
    }
}