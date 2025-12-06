package com.example.domain.repository.nonfeature.auth

interface AuthRepository {

    suspend fun saveTokens(accessToken: String, refreshToken: String): Result<Unit>
    suspend fun getAccessToken(): Result<String?>
    suspend fun getRefreshToken(): Result<String?>
    suspend fun clearTokens(): Result<Unit>

    suspend fun refreshToken(refreshToken: String): Result<Pair<String?, String?>>

}