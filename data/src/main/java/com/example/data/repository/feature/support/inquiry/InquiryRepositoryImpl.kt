package com.example.data.repository.feature.support.inquiry

import com.example.data.datasource.remote.network.feature.support.inquiry.InquiryApi
import com.example.data.datasource.remote.network.feature.support.inquiry.dto.InquiryResDto
import com.example.data.datasource.remote.network.common.safeApiCall
import com.example.data.mapper.feature.support.toDto
import com.example.domain.model.feature.support.inquiry.InquiryRequest
import com.example.domain.repository.feature.support.InquiryRepository
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