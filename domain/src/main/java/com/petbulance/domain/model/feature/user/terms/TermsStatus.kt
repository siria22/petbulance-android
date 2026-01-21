package com.petbulance.domain.model.feature.user.terms

data class TermsStatus(
    val service: Boolean,
    val privacy: Boolean,
    val location: Boolean,
    val marketing: Boolean
)