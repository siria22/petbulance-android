package com.petbulance.domain.usecase.feature.user.terms

import com.petbulance.domain.model.feature.user.terms.TermsStatus
import com.petbulance.domain.repository.feature.user.TermsRepository
import javax.inject.Inject

class GetTermsStatusUseCase @Inject constructor(
    private val repository: TermsRepository
) {
    suspend operator fun invoke(): Result<TermsStatus> {
        return repository.getTermsStatus()
    }
}