package com.petbulance.data.repository.feature.user.terms

import com.petbulance.data.datasource.local.database.dao.TermConsentDao
import com.petbulance.data.datasource.local.database.entity.TermConsentEntity
import com.petbulance.data.datasource.remote.network.common.safeApiCall
import com.petbulance.data.datasource.remote.network.feature.user.terms.TermsApi
import com.petbulance.data.datasource.remote.network.feature.user.terms.dto.TermDto
import com.petbulance.data.datasource.remote.network.feature.user.terms.dto.TermsConsentRequestDto
import com.petbulance.data.datasource.remote.network.feature.user.terms.dto.TermsStatusResponseDto
import com.petbulance.domain.model.feature.user.terms.Term
import com.petbulance.domain.model.feature.user.terms.TermsStatus
import com.petbulance.domain.model.type.TermsType
import com.petbulance.domain.repository.feature.user.TermsRepository
import javax.inject.Inject

class TermsRepositoryImpl @Inject constructor(
    private val api: TermsApi,
    private val termConsentDao: TermConsentDao
) : TermsRepository {

    override suspend fun getTermsStatus(): Result<TermsStatus> {
        return safeApiCall<TermsStatusResponseDto>("terms/status") {
            api.getTermsStatus()
        }.map { dto ->
            TermsStatus(
                service = dto.service == "AGREE",
                privacy = dto.privacy == "AGREE",
                location = dto.location == "AGREE",
                marketing = dto.marketing == "AGREE"
            )
        }
    }

    override suspend fun getTermsList(): Result<List<Term>> {
        return safeApiCall<List<TermDto>>("terms") {
            api.getTermsList()
        }.map { dtoList ->
            dtoList.map { dto ->
                Term(
                    id = dto.id,
                    title = dto.title,
                    termsType = runCatching {
                        TermsType.valueOf(
                            dto.termsType ?: ""
                        )
                    }.getOrNull(), // String -> Enum 변환
                    required = dto.required,
                    summary = dto.summary,
                    content = dto.content ?: "",
                    version = dto.version
                )
            }
        }
    }

    override suspend fun getTermDetail(type: String): Result<Term> {
        return safeApiCall<TermDto>("terms/$type") {
            api.getTermDetail(type)
        }.map { dto ->
            Term(
                id = dto.id,
                title = dto.title,
                termsType = runCatching { TermsType.valueOf(dto.termsType ?: "") }.getOrNull(),
                required = dto.required,
                summary = dto.summary,
                content = dto.content ?: "",
                version = dto.version
            )
        }
    }

    override suspend fun saveTermsConsent(termsIdList: List<Long>): Result<Unit> {
        return safeApiCall<Unit>("terms/consents") {
            api.saveTermsConsent(TermsConsentRequestDto(termsIdList))
        }
    }

    override suspend fun saveTermsConsentLocal(terms: List<Term>): Result<Unit> = runCatching {
        val entities = terms.map { term ->
            TermConsentEntity(
                version = term.version,
                title = term.title,
                content = term.summary,
                agreedDate = System.currentTimeMillis()
            )
        }
        termConsentDao.insertConsents(entities)
    }

    override suspend fun withdrawTermsConsent(type: String): Result<Unit> {
        return safeApiCall<Unit>("terms/$type") {
            api.withdrawTermsConsent(type)
        }
    }
}