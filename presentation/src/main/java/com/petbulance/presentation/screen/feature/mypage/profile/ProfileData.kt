package com.petbulance.presentation.screen.feature.mypage.profile

data class ProfileData(
    val data: String
) {
    companion object {
        val empty = ProfileData(
            data = ""
        )

        fun stub() = ProfileData(
            data = "stub data"
        )
    }
}