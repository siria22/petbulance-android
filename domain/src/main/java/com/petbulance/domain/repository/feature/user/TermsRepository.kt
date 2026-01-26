package com.petbulance.domain.repository.feature.user

import com.petbulance.domain.model.feature.user.terms.Term
import com.petbulance.domain.model.feature.user.terms.TermsStatus


interface TermsRepository {
    suspend fun getTermsStatus(): Result<TermsStatus>
    suspend fun getTermsList(): Result<List<Term>>
    suspend fun getTermDetail(type: String): Result<Term>
    suspend fun saveTermsConsent(termsTypeList: List<Long>): Result<Unit>
    suspend fun saveTermsConsentLocal(terms: List<Term>): Result<Unit>

    suspend fun withdrawTermsConsent(type: String): Result<Unit>
}