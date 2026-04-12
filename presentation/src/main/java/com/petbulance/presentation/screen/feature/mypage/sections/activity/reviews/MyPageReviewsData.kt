package com.petbulance.presentation.screen.feature.mypage.sections.activity.reviews

data class MyPageReviewsData(
    val data: String
) {
    companion object {
        val empty = MyPageReviewsData(
            data = ""
        )

        fun stub() = MyPageReviewsData(
            data = "stub data"
        )
    }
}