package com.example.domain.usecase.feature.user.terms

import com.example.domain.model.feature.user.terms.TermsStatus
import com.example.domain.repository.feature.user.TermsRepository
import javax.inject.Inject

class GetTermsStatusUseCase @Inject constructor(
    private val repository: TermsRepository
) {
    suspend operator fun invoke(): Result<TermsStatus> {
        return repository.getTermsStatus()
    }
}