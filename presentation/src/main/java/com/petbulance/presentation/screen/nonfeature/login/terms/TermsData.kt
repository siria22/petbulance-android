package com.petbulance.presentation.screen.nonfeature.login.terms

import com.petbulance.domain.model.feature.user.terms.Term

data class TermsData(
    val termsList: List<Term>,
    val agreedTermIds: Set<Long>,
    val isAllRequiredAgreed: Boolean,
    val currentTerm: Term? = null,
    val userTempName: String
) {
    companion object {
        fun empty() = TermsData(
            termsList = emptyList(),
            agreedTermIds = emptySet(),
            isAllRequiredAgreed = false,
            currentTerm = null,
            userTempName = "따뜻한햄스터07"
        )

        fun stub() = TermsData(
            termsList = listOf(
                Term.stub()
            ),
            agreedTermIds = emptySet(),
            isAllRequiredAgreed = false,
            currentTerm = null,
            userTempName = "따뜻한햄스터07"
        )
    }
}