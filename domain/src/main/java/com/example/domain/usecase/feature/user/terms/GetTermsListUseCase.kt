package com.example.domain.usecase.feature.user.terms

import com.example.domain.model.feature.user.terms.Term
import com.example.domain.repository.feature.user.TermsRepository
import javax.inject.Inject

class GetTermsListUseCase @Inject constructor(
    private val repository: TermsRepository
) {
    suspend operator fun invoke(): Result<List<Term>> {
        return repository.getTermsList()
    }
}