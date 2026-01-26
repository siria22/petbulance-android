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
)