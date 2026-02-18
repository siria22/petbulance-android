package com.petbulance.presentation.screen.feature.mypage.sections.user.profile

import android.net.Uri
import com.petbulance.domain.model.feature.user.user.UserInfo

data class MyPageProfileData(
    val userInfo: UserInfo?,
    val selectedImageUri: Uri?
) {
    companion object {
        val empty = MyPageProfileData(
            userInfo = null,
            selectedImageUri = null
        )

        fun stub() = MyPageProfileData(
            userInfo = UserInfo.stub,
            selectedImageUri = null
        )
    }
}