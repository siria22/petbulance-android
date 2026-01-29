package com.petbulance.domain.model.feature.user.user

data class UserInfo(
    val nickname: String,
    val profileImageUrl: String?,
    val email: String,
    val provider: String,
    val connectedSocials: ConnectedSocials
) {
    companion object {
        val stub = UserInfo(
            nickname = "siriissosirisiri",
            profileImageUrl = null,
            email = "cmsiria22@gmail.com",
            provider = "KAKAO",
            connectedSocials = ConnectedSocials(null, null, null)
        )
    }
}
