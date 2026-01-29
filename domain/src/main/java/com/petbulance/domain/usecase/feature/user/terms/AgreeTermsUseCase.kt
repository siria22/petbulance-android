package com.petbulance.domain.usecase.feature.user.terms

import com.petbulance.domain.model.feature.user.terms.Term
import com.petbulance.domain.repository.feature.user.TermsRepository
import javax.inject.Inject

class AgreeTermsUseCase @Inject constructor(
    private val repository: TermsRepository
) {
    suspend operator fun invoke(terms: List<Term>): Result<Unit> {
        return runCatching {
            repository.saveTermsConsentLocal(terms).getOrThrow()

            val termsIds = terms.map { it.id }
            repository.saveTermsConsent(termsIds).getOrThrow()
        }
    }
}