package com.example.presentation.utils.nav

sealed class ScreenDestinations(val route: String) {

    data object Home : ScreenDestinations("home") {
        data object Test : ScreenDestinations("home/test")
    }

}