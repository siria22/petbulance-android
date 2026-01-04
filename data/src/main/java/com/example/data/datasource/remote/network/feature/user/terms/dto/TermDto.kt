package com.example.data.datasource.remote.network.feature.user.terms.dto

import kotlinx.serialization.Serializable

@Serializable
data class TermDto(
    val id: Long,
    val title: String,
    val required: Boolean,
    val summary: String,
    val content: String? = null,
    val version: String
)