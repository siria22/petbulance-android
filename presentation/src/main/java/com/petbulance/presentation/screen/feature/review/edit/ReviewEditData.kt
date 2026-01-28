package com.petbulance.presentation.screen.feature.review.edit

data class ReviewEditData(
    val data: String
) {
    companion object {
        val empty = ReviewEditData(
            data = ""
        )

        fun stub() = ReviewEditData(
            data = "stub data"
        )
    }
}