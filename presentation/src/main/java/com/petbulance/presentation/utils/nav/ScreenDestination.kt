package com.petbulance.presentation.utils.nav

import com.petbulance.domain.model.type.AnimalCategory

sealed class ScreenDestinations(val route: String) {

    data object Splash : ScreenDestinations("splash")

    data object Login : ScreenDestinations("login")
    data object Welcome : ScreenDestinations("welcome")

    data object Home : ScreenDestinations("home?checkTerms={checkTerms}") {
        const val ARG_CHECK_TERMS = "checkTerms"

        fun createRoute(checkTerms: Boolean = false): String {
            return "home?$ARG_CHECK_TERMS=$checkTerms"
        }
    }

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

    data object Review : ScreenDestinations("review") {
        data object Search : ScreenDestinations("review/search")
        data object Create : ScreenDestinations("review/create?data={data}") {
            const val ARG_DATA = "data"

            fun createRoute(data: String? = null): String {
                return if (data == null) "review/create"
                else "review/create?data=$data"
            }
        }
        data object ReceiptCamera : ScreenDestinations("review/receipt_camera")

        data object Edit : ScreenDestinations("review/edit/{id}") {
            const val ARG_ID = "id"
            fun createRoute(id: Long): String = "review/edit/$id"
        }
    }
}