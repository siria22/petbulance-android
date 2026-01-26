package com.petbulance.domain.model.feature.user.terms

import com.petbulance.domain.model.type.TermsType

data class Term(
    val id: Long,
    val title: String,
    val termsType: TermsType?,
    val required: Boolean,
    val summary: String,
    val content: String = "",
    val version: String
) {
    companion object {
        fun stub() = Term(
            id = 1,
            title = "약관 1",
            required = true,
            summary = "asdfasdf",
            content = "약관 1 내용",
            version = "1.0",
            termsType = TermsType.SERVICE
        )
    }
}