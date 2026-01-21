package com.petbulance.domain.model.feature.user.terms

data class Term(
    val id: Long,
    val title: String,
    val required: Boolean,
    val summary: String,
    val content: String = "",
    val version: String
)