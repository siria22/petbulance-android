package com.example.presentation.utils.nav

sealed class ScreenDestinations(val route: String) {

    data object Home : ScreenDestinations("home")

    data object Search : ScreenDestinations("search") {
        data object HospitalInfo : ScreenDestinations("search/hospital/{id}") {
            const val ARG_ID: String = "id"
            fun createRoute(id: Long): String = "search/hospital/$id"
        }
    }

}