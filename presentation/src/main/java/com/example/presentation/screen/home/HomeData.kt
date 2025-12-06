package com.example.presentation.screen.home

data class HomeData(
    val data: String
) {
    companion object {
        fun empty() = HomeData(
            data = ""
        )
    }
}