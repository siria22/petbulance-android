package com.example.domain.usecase.feature.user.terms

import com.example.domain.repository.feature.user.TermsRepository
import javax.inject.Inject

class SaveTermsConsentUseCase @Inject constructor(
    private val repository: TermsRepository
) {
    suspend operator fun invoke(termsIdList: List<Long>): Result<Unit> {
        return repository.saveTermsConsent(termsIdList)
    }
}