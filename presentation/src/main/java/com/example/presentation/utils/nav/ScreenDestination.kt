package com.example.presentation.utils.nav

import com.example.domain.model.type.AnimalCategory

sealed class ScreenDestinations(val route: String) {

    data object Home : ScreenDestinations("home")

    data object Search : ScreenDestinations("search?animal={animal}") {
        const val ARG_ANIMAL = "animal"

        fun createRoute(animal: AnimalCategory? = null): String {
            return if (animal == null) "search"
            else "search?animal=${animal.name}"
        }

        data object HospitalInfo : ScreenDestinations("search/hospital/{id}") {
            const val ARG_ID: String = "id"
            fun createRoute(id: Long): String = "search/hospital/$id"
        }
    }

}