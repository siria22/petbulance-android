package com.petbulance.presentation.screen.feature.mypage.main

import com.petbulance.domain.model.feature.user.user.UserInfo

data class MyPageData(
    val userInfo: UserInfo?,
    val currentVersion: String,
    val latestVersion: String
) {
    companion object {
        val empty = MyPageData(
            userInfo = null,
            currentVersion = "",
            latestVersion = ""
        )

        fun stub() = MyPageData(
            userInfo = UserInfo.stub,
            currentVersion = "1.0.0",
            latestVersion = "1.0.0"
        )
    }
}