package com.petbulance.data.repository.feature.user.terms

import com.petbulance.data.datasource.local.database.dao.TermConsentDao
import com.petbulance.data.datasource.local.database.dao.TermsCacheDao
import com.petbulance.data.datasource.local.database.dao.TermsStatusDao
import com.petbulance.data.datasource.local.database.entity.TermConsentEntity
import com.petbulance.data.datasource.local.database.entity.TermsCacheEntity
import com.petbulance.data.datasource.local.database.entity.TermsStatusEntity
import com.petbulance.data.datasource.remote.network.common.safeApiCall
import com.petbulance.data.datasource.remote.network.feature.user.terms.TermsApi
import com.petbulance.data.datasource.remote.network.feature.user.terms.dto.TermDto
import com.petbulance.data.datasource.remote.network.feature.user.terms.dto.TermsConsentRequestDto
import com.petbulance.data.datasource.remote.network.feature.user.terms.dto.TermsStatusResponseDto
import com.petbulance.domain.model.feature.user.terms.Term
import com.petbulance.domain.model.feature.user.terms.TermsStatus
import com.petbulance.domain.model.type.TermsType
import com.petbulance.domain.repository.feature.user.TermsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TermsRepositoryImpl @Inject constructor(
    private val api: TermsApi,
    private val termConsentDao: TermConsentDao,
    private val termsCacheDao: TermsCacheDao,
    private val termsStatusDao: TermsStatusDao
) : TermsRepository {

    companion object {
        private const val CACHE_VALIDITY_MS = 24 * 60 * 60 * 1000L // 24시간
    }

    override suspend fun getTermsStatus(): Result<TermsStatus> {
        // 캐시 확인
        val cached = withContext(Dispatchers.IO) {
            termsStatusDao.getTermsStatus()
        }
        val currentTime = System.currentTimeMillis()

        // 캐시가 유효한 경우 로컬 데이터 반환
        if (cached != null && (currentTime - cached.cachedAt) < CACHE_VALIDITY_MS) {
            return Result.success(
                TermsStatus(
                    service = cached.service,
                    privacy = cached.privacy,
                    location = cached.location,
                    marketing = cached.marketing
                )
            )
        }

        // API 호출
        return safeApiCall<TermsStatusResponseDto>("terms/status") {
            api.getTermsStatus()
        }.map { dto ->
            val status = TermsStatus(
                service = dto.service == "AGREE",
                privacy = dto.privacy == "AGREE",
                location = dto.location == "AGREE",
                marketing = dto.marketing == "AGREE"
            )

            // 캐시에 저장
            withContext(Dispatchers.IO) {
                termsStatusDao.insert(
                    TermsStatusEntity(
                        service = status.service,
                        privacy = status.privacy,
                        location = status.location,
                        marketing = status.marketing,
                        cachedAt = currentTime
                    )
                )
            }

            status
        }
    }

    override suspend fun getTermsList(): Result<List<Term>> {
        // 캐시 확인
        val latestCacheTime = withContext(Dispatchers.IO) {
            termsCacheDao.getLatestCacheTime()
        }
        val currentTime = System.currentTimeMillis()

        // 캐시가 유효한 경우 로컬 데이터 반환
        if (latestCacheTime != null && (currentTime - latestCacheTime) < CACHE_VALIDITY_MS) {
            return withContext(Dispatchers.IO) {
                val cachedTerms = termsCacheDao.getAllTerms().map { entity ->
                    Term(
                        id = entity.id,
                        title = entity.title,
                        termsType = entity.termsType?.let { runCatching { TermsType.valueOf(it) }.getOrNull() },
                        required = entity.required,
                        summary = entity.summary,
                        content = entity.content,
                        version = entity.version
                    )
                }
                Result.success(cachedTerms)
            }
        }

        // API 호출
        return safeApiCall<List<TermDto>>("terms") {
            api.getTermsList()
        }.map { dtoList ->
            val terms = dtoList.map { dto ->
                Term(
                    id = dto.id,
                    title = dto.title,
                    termsType = runCatching {
                        TermsType.valueOf(
                            dto.termsType ?: ""
                        )
                    }.getOrNull(),
                    required = dto.required,
                    summary = dto.summary,
                    content = dto.content ?: "",
                    version = dto.version
                )
            }

            // 캐시에 저장
            withContext(Dispatchers.IO) {
                termsCacheDao.deleteAll()
                val cacheEntities = terms.map { term ->
                    TermsCacheEntity(
                        id = term.id,
                        title = term.title,
                        termsType = term.termsType?.name,
                        required = term.required,
                        summary = term.summary,
                        content = term.content,
                        version = term.version,
                        cachedAt = currentTime
                    )
                }
                termsCacheDao.insertAll(cacheEntities)
            }

            terms
        }
    }

    override suspend fun getTermDetail(type: String): Result<Term> {
        // 캐시 확인
        val cached = withContext(Dispatchers.IO) {
            termsCacheDao.getTermByType(type)
        }
        val currentTime = System.currentTimeMillis()

        // 캐시가 유효하고 내용이 비어있지 않은 경우에만 로컬 데이터 반환
        if (cached != null && (currentTime - cached.cachedAt) < CACHE_VALIDITY_MS && cached.content.isNotBlank()) {
            return Result.success(
                Term(
                    id = cached.id,
                    title = cached.title,
                    termsType = cached.termsType?.let { runCatching { TermsType.valueOf(it) }.getOrNull() },
                    required = cached.required,
                    summary = cached.summary,
                    content = cached.content,
                    version = cached.version
                )
            )
        }

        // 캐시가 없거나 만료된 경우, 전체 약관 목록을 API로 받아와서 갱신 후 해당 약관 반환
        return fetchAndCacheTermsList().mapCatching { terms ->
            terms.find { it.termsType?.name == type }
                ?: throw IllegalStateException("해당 약관을 찾을 수 없습니다: $type")
        }
    }

    override suspend fun saveTermsConsent(termsTypeList: List<Long>): Result<Unit> {
        return safeApiCall<Unit>("terms/consents") {
            api.saveTermsConsent(TermsConsentRequestDto(termsTypeList))
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

    private suspend fun fetchAndCacheTermsList(): Result<List<Term>> {
        val currentTime = System.currentTimeMillis()
        
        return safeApiCall<List<TermDto>>("terms") {
            api.getTermsList()
        }.map { dtoList ->
            val terms = dtoList.map { dto ->
                Term(
                    id = dto.id,
                    title = dto.title,
                    termsType = runCatching {
                        TermsType.valueOf(dto.termsType ?: "")
                    }.getOrNull(),
                    required = dto.required,
                    summary = dto.summary,
                    content = dto.content ?: dto.summary,
                    version = dto.version
                )
            }

            // 캐시에 저장
            withContext(Dispatchers.IO) {
                termsCacheDao.deleteAll()
                val cacheEntities = terms.map { term ->
                    TermsCacheEntity(
                        id = term.id,
                        title = term.title,
                        termsType = term.termsType?.name,
                        required = term.required,
                        summary = term.summary,
                        content = term.content,
                        version = term.version,
                        cachedAt = currentTime
                    )
                }
                termsCacheDao.insertAll(cacheEntities)
            }

            terms
        }
    }
}