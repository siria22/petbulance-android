package com.petbulance.presentation.screen.nonfeature.login.terms

import com.petbulance.domain.model.feature.user.terms.Term

data class TermsData(
    val termsList: List<Term>,
    val agreedTermIds: Set<Long>,
    val isAllRequiredAgreed: Boolean,
    val currentTerm: Term? = null
) {
    companion object {
        fun empty() = TermsData(
            termsList = emptyList(),
            agreedTermIds = emptySet(),
            isAllRequiredAgreed = false,
            currentTerm = null
        )

        fun stub() = TermsData(
            termsList = listOf(
                Term(
                    id = 1,
                    title = "약관 1",
                    required = true,
                    summary = "asdfasdf",
                    content = "약관 1 내용",
                    version = "1.0"
                )
            ),
            agreedTermIds = emptySet(),
            isAllRequiredAgreed = false,
            currentTerm = null
        )
    }
}