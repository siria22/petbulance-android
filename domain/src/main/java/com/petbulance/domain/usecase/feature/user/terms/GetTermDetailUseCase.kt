package com.petbulance.domain.usecase.feature.user.terms

import com.petbulance.domain.model.feature.user.terms.Term
import com.petbulance.domain.repository.feature.user.TermsRepository
import javax.inject.Inject

class GetTermDetailUseCase @Inject constructor(
    private val repository: TermsRepository
) {
    suspend operator fun invoke(type: String): Result<Term> {
        return repository.getTermDetail(type)
    }
}