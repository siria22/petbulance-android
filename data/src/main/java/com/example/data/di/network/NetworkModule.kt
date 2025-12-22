package com.example.data.di.network

import android.util.Log
import com.example.domain.repository.nonfeature.auth.AuthRepository
import com.example.domain.utils.LOGGER_TAG
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import javax.inject.Provider
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultHttpClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    val logger = "$LOGGER_TAG - NetworkModule"
    private const val TOLERABLE_TIME = 3000L

    @Provides
    @Singleton
    @DefaultHttpClient
    fun provideDefaultHttpClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
            install(HttpTimeout) {
                requestTimeoutMillis = TOLERABLE_TIME
                connectTimeoutMillis = TOLERABLE_TIME
                socketTimeoutMillis = TOLERABLE_TIME
            }
            defaultRequest {
                accept(ContentType.Application.Json)
            }
        }
    }

    @Provides
    @Singleton
    @AuthHttpClient
    fun provideAuthHttpClient(
        authRepositoryProvider: Provider<AuthRepository>
    ): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
            install(HttpTimeout) {
                requestTimeoutMillis = TOLERABLE_TIME
                connectTimeoutMillis = TOLERABLE_TIME
                socketTimeoutMillis = TOLERABLE_TIME
            }
            defaultRequest {
                accept(ContentType.Application.Json)
            }

            // Auth 플러그인 추가
            install(Auth) {
                bearer {
                    loadTokens {
                        val authRepository = authRepositoryProvider.get()
                        val accessToken =
                            runBlocking { authRepository.getAccessToken() }.getOrNull()
                        val refreshToken =
                            runBlocking { authRepository.getRefreshToken() }.getOrNull()
                        if (accessToken.isNullOrBlank() || refreshToken.isNullOrBlank()) {
                            null
                        } else {
                            BearerTokens(accessToken, refreshToken)
                        }
                    }
                    refreshTokens {
                        val authRepository = authRepositoryProvider.get()
                        val oldRefreshToken =
                            runBlocking { authRepository.getRefreshToken() }.getOrNull()
                        if (oldRefreshToken.isNullOrBlank()) {
                            return@refreshTokens null
                        }

                        val tokenResult =
                            runBlocking { authRepository.refreshToken(oldRefreshToken) }

                        tokenResult.fold(
                            onSuccess = { (newAccessToken, newRefreshToken) ->
                                if (newAccessToken != null && newRefreshToken != null) {
                                    runBlocking {
                                        authRepository.saveTokens(newAccessToken, newRefreshToken)
                                    }
                                    BearerTokens(newAccessToken, newRefreshToken)
                                } else {
                                    runBlocking { authRepository.clearTokens() }
                                    null
                                }
                            },
                            onFailure = {
                                runBlocking { authRepository.clearTokens() }
                                null
                            }
                        )
                    }
                    sendWithoutRequest { request ->
                        val path = request.url.encodedPath
                        val isAuthRequest =
                            path.contains("/auth/login") || path.contains("/auth/refresh")
                        Log.d(
                            logger, "Request path: $path, " +
                                    "Is Auth Request: $isAuthRequest (Send token: ${!isAuthRequest})"
                        )
                        !isAuthRequest
                    }
                }
            }
        }
    }
}