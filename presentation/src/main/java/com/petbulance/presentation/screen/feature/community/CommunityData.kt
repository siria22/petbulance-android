package com.petbulance.presentation.screen.feature.community

data class CommunityData(
    val data: String
) {
    companion object {
        val empty = CommunityData(
            data = ""
        )

        fun stub() = CommunityData(
            data = "stub data"
        )
    }
}