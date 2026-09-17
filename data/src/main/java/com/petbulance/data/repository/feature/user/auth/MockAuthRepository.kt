package com.petbulance.data.repository.feature.user.auth

import com.petbulance.domain.model.feature.user.auth.SocialLoginResult
import com.petbulance.domain.model.type.LoginProviderType
import com.petbulance.domain.repository.feature.user.AuthRepository
import javax.inject.Inject

/**
 * 서버 종료 후 항상 로그인된 상태로 동작한다.
 * 토큰이 늘 존재하므로 스플래시에서 로그인 화면을 거치지 않고 홈으로 이동한다.
 */
class MockAuthRepository @Inject constructor() : AuthRepository {
    override suspend fun saveTokens(accessToken: String, refreshToken: String): Result<Unit> = Result.success(Unit)
    override suspend fun getAccessToken(): Result<String?> = Result.success("mock_access_token")
    override suspend fun getRefreshToken(): Result<String?> = Result.success("mock_refresh_token")
    override suspend fun clearTokens(): Result<Unit> = Result.success(Unit)
    override suspend fun refreshToken(refreshToken: String): Result<Pair<String?, String?>> =
        Result.success("new_access_token" to "new_refresh_token")

    override suspend fun socialLogin(provider: LoginProviderType, authCode: String): Result<SocialLoginResult> =
        Result.success(SocialLoginResult(isNewUser = false, accessToken = "mock_token", refreshToken = "mock_token"))

    override suspend fun logout(): Result<Unit> = Result.success(Unit)
    override fun isLoggingOut(): Boolean = false
    override suspend fun saveLastLoginPlatform(platform: LoginProviderType): Result<Unit> = Result.success(Unit)
    override suspend fun getLastLoginPlatform(): Result<LoginProviderType?> = Result.success(LoginProviderType.KAKAO)
    override suspend fun setAutoLoginEnabled(isEnabled: Boolean): Result<Unit> = Result.success(Unit)
    override suspend fun isAutoLoginEnabled(): Result<Boolean> = Result.success(true)
}