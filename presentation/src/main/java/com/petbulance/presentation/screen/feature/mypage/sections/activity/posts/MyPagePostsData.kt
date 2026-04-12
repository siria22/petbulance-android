package com.petbulance.presentation.screen.feature.mypage.sections.activity.posts

data class MyPagePostsData(
    val data: String
) {
    companion object {
        val empty = MyPagePostsData(
            data = ""
        )

        fun stub() = MyPagePostsData(
            data = "stub data"
        )
    }
}
