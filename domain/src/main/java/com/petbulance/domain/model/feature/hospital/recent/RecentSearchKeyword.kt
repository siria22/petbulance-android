package com.petbulance.domain.model.feature.hospital.recent

data class RecentSearchKeyword(
    val id: Long,
    val keyword: String,
    val date: String
): ContentAsString {
    override fun getContentAsString(): String = keyword
}
