package com.petbulance.presentation.screen.feature.mypage.sections.activity.comments

data class MyPageCommentsData(
    val data: String
) {
    companion object {
        val empty = MyPageCommentsData(
            data = ""
        )

        fun stub() = MyPageCommentsData(
            data = "stub data"
        )
    }
}
