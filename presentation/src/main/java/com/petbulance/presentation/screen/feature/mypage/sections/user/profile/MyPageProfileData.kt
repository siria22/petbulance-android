package com.petbulance.presentation.screen.feature.mypage.sections.user.profile

data class MyPageProfileData(
    val data: String
) {
    companion object {
        val empty = MyPageProfileData(
            data = ""
        )

        fun stub() = MyPageProfileData(
            data = "stub data"
        )
    }
}