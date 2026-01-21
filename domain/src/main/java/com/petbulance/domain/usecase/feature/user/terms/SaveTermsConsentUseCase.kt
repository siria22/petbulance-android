package com.petbulance.domain.usecase.feature.user.terms

import com.petbulance.domain.repository.feature.user.TermsRepository
import javax.inject.Inject

class SaveTermsConsentUseCase @Inject constructor(
    private val repository: TermsRepository
) {
    suspend operator fun invoke(termsIdList: List<Long>): Result<Unit> {
        return repository.saveTermsConsent(termsIdList)
    }
}