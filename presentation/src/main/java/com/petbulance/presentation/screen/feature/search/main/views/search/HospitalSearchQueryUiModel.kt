package com.petbulance.presentation.screen.feature.search.main.views.search

import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.domain.model.type.Region

data class HospitalSearchQueryUiModel(
    val query: String?,
    val region: Region?,
    val district: String?,
    val animalCategories: List<AnimalCategory>,
    val openNowOnly: Boolean?
) {
    fun getRegionFilter(): String {
        return if (region != null) {
            "${region.name} ${district ?: ""}"
        } else {
            "전체"
        }
    }

    companion object {
        val empty = HospitalSearchQueryUiModel(
            query = null,
            region = null,
            district = null,
            animalCategories = emptyList(),
            openNowOnly = null
        )
    }
}