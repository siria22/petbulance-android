package com.petbulance.domain.usecase.feature.user.terms

import com.petbulance.domain.repository.feature.user.TermsRepository
import javax.inject.Inject

class WithdrawTermsConsentUseCase @Inject constructor(
    private val repository: TermsRepository
) {
    suspend operator fun invoke(type: String): Result<Unit> {
        return repository.withdrawTermsConsent(type)
    }
}
