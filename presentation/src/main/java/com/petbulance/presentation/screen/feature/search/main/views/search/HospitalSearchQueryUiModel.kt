package com.petbulance.presentation.screen.feature.search.main.views.search

import com.petbulance.domain.model.type.AnimalCategory
import com.petbulance.domain.model.type.Region

data class HospitalSearchQueryUiModel(
    val query: String?,
    val region: Region?,
    val district: String?,
    val animalCategory: AnimalCategory?,
    val openNowOnly: Boolean?
) {
    // TODO : Backend logics
    fun getRegionFilter() : String {
        return if (region != null){
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
            animalCategory = null,
            openNowOnly = null
        )
    }
}