package com.petbulance.data.di.network

import android.util.Log
import com.petbulance.domain.repository.feature.user.AuthRepository
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
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
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

    private const val TOLERABLE_TIME = 30_000L
    private const val LOG_TAG = "siria22 - NetworkModule"

    private const val isLoggingOn = true

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
                if (isLoggingOn) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            Log.d(LOG_TAG, message)
                        }
                    }
                    level = LogLevel.ALL
                }
            }
            install(HttpTimeout) {
                requestTimeoutMillis = TOLERABLE_TIME
                connectTimeoutMillis = TOLERABLE_TIME
                socketTimeoutMillis = TOLERABLE_TIME
            }
            defaultRequest {
                header(HttpHeaders.Accept, "*/*")
                contentType(ContentType.Application.Json)
//                header(HttpHeaders.UserAgent, "Petbulance-Android-Client")
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
                if (isLoggingOn) {
                    logger = object : Logger {
                        override fun log(message: String) {
                            Log.d(LOG_TAG, message)
                        }
                    }
                    level = LogLevel.ALL
                }
            }

            install(HttpTimeout) {
                requestTimeoutMillis = TOLERABLE_TIME
                connectTimeoutMillis = TOLERABLE_TIME
                socketTimeoutMillis = TOLERABLE_TIME
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
                header(HttpHeaders.Accept, "*/*")
//                header(HttpHeaders.UserAgent, "Petbulance-Android-Client")
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val authRepository = authRepositoryProvider.get()
                        val accessToken = authRepository.getAccessToken().getOrNull()
                        val refreshToken = authRepository.getRefreshToken().getOrNull()

                        if (accessToken.isNullOrBlank() || refreshToken.isNullOrBlank()) {
                            null
                        } else {
                            BearerTokens(accessToken, refreshToken)
                        }
                    }

                    refreshTokens {
                        Log.d(LOG_TAG, "Token expired. Refreshing tokens...")
                        val authRepository = authRepositoryProvider.get()
                        val refreshToken = authRepository.getRefreshToken().getOrNull()

                        if (refreshToken.isNullOrBlank()) {
                            Log.e(LOG_TAG, "No refresh token found.")
                            return@refreshTokens null
                        }

                        val result = authRepository.refreshToken(refreshToken)

                        var newTokens: BearerTokens? = null

                        result.onSuccess { (newAccess, newRefreshToken) ->
                            if (!newAccess.isNullOrBlank()) {
                                val finalRefreshToken = newRefreshToken ?: refreshToken
                                authRepository.saveTokens(newAccess, finalRefreshToken)
                                newTokens = BearerTokens(newAccess, finalRefreshToken)
                                Log.d(LOG_TAG, "Token refresh successful.")
                            }
                        }.onFailure {
                            Log.e(LOG_TAG, "Token refresh failed: ${it.message}")
                            authRepository.clearTokens()
                        }

                        newTokens
                    }

                    sendWithoutRequest { request ->
                        val path = request.url.encodedPath
                        path.contains("auth/login")
                                || path.contains("auth/refresh")
                                || path.contains("auth/social/login")
                    }
                }
            }
        }
    }
}