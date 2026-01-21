package com.petbulance.presentation.component.ui.molecule

import com.petbulance.domain.model.type.BaseEnumType

enum class FilterBottomSheetTab(val korean: String) : BaseEnumType {
    REGION("지역"), SPECIES("동물종");

    override fun fromString(value: String): BaseEnumType {
        return FilterBottomSheetTab.entries.find { it.name.equals(value, ignoreCase = true) }
            ?: SPECIES
    }

    override fun toKorean(): String = korean
}