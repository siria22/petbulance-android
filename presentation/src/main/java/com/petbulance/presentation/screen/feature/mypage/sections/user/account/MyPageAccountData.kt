import com.petbulance.domain.model.feature.user.user.ConnectedSocials
import com.petbulance.domain.model.feature.user.user.UserInfo
import com.petbulance.domain.model.type.LoginProviderType

data class MyPageAccountData(
    val userInfo: UserInfo?,
    val isAutoLoginEnabled: Boolean
) {
    companion object {
        val empty = MyPageAccountData(
            userInfo = UserInfo(
                nickname = "siria22",
                profileImageUrl = null,
                email = "cmsiria22@gmail.com",
                provider = LoginProviderType.GOOGLE.name,
                connectedSocials = ConnectedSocials(
                    kakao = "kaka@kakao.com",
                    google = "gogo@google.com",
                    naver = "nana@naver.com"
                )
            ),
            isAutoLoginEnabled = true
        )
    }
}