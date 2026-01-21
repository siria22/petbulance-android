package com.petbulance.domain.usecase.feature.user.terms

import com.petbulance.domain.model.feature.user.terms.Term
import com.petbulance.domain.repository.feature.user.TermsRepository
import javax.inject.Inject

class GetTermsListUseCase @Inject constructor(
    private val repository: TermsRepository
) {
    suspend operator fun invoke(): Result<List<Term>> {
        return repository.getTermsList()
    }
}