package com.example.presentation.utils.nav

import com.example.domain.model.type.AnimalCategory

sealed class ScreenDestinations(val route: String) {

    data object Splash : ScreenDestinations("splash")

    data object Login : ScreenDestinations("login")
    data object Terms : ScreenDestinations("terms")

    data object Home : ScreenDestinations("home")

    data object Search : ScreenDestinations("search?animal={animal}") {
        const val ARG_ANIMAL = "animal"

        fun createRoute(animal: AnimalCategory = AnimalCategory.ALL): String {
            return if (animal == AnimalCategory.ALL) "search"
            else "search?animal=${animal.name}"
        }

        data object HospitalInfo : ScreenDestinations("search/hospital/{id}") {
            const val ARG_ID: String = "id"
            fun createRoute(id: Long): String = "search/hospital/$id"
        }
    }

    data object Review : ScreenDestinations("review")
}