package com.petbulance.domain.usecase.feature.user.terms

import com.petbulance.domain.model.feature.user.terms.Term
import com.petbulance.domain.repository.feature.user.TermsRepository
import javax.inject.Inject

class AgreeTermsUseCase @Inject constructor(
    private val repository: TermsRepository
) {
    suspend operator fun invoke(terms: List<Term>): Result<Unit> {
        return runCatching {
            // 1. 로컬 DB에 저장
            repository.saveTermsConsentLocal(terms).getOrThrow()

            // 2. 서버로 동의 내역 전송
            val termsType = terms.map { (it.termsType?.ordinal?.toLong()?.plus(1L)) ?: 0L }
            repository.saveTermsConsent(termsType).getOrThrow()
        }
    }
}