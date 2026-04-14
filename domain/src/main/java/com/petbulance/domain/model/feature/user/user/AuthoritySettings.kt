package com.petbulance.domain.model.feature.user.user

data class AuthoritySettings(
    val locationService: Boolean,
    val marketing: Boolean,
    val camera: Boolean
)
