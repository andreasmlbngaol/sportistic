package com.jawapbo.sportistic.core.data

import android.util.Log
import com.jawapbo.sportistic.shared.data.auth.RefreshTokenResponse
import com.jawapbo.sportistic.shared.data.auth.RefreshTokenRequest
import com.jawapbo.sportistic.shared.data.core.LocalDateTimeSerializer
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpSend
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.plugins.plugin
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import kotlinx.serialization.modules.SerializersModule
import java.time.LocalDateTime

object HttpClientFactory {
    @OptIn(ExperimentalSerializationApi::class)
    fun create(
        engine: HttpClientEngine,
        dataStore: AuthDataStoreManager
    ): HttpClient {
        val client =  HttpClient(engine) {
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = true
                        isLenient = true
                        namingStrategy = JsonNamingStrategy.SnakeCase
                        serializersModule = SerializersModule {
                            contextual(LocalDateTime::class, LocalDateTimeSerializer)
                        }
                    }
                )
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
        client.plugin(HttpSend).intercept { request ->
            Log.d("HttpSendInterceptor", "Intercepting request to ${request.url}")

            // Get fresh token dari DataStore untuk setiap request
            val tokens =  dataStore.getTokens().tokens
            val accessToken = tokens?.accessToken

            Log.d("HttpSendInterceptor", "Current stored token: ${accessToken?.take(20)}...")

            // Remove existing Authorization header jika ada
            request.headers.remove(HttpHeaders.Authorization)

            // Set token yang fresh jika ada
            if (!accessToken.isNullOrBlank()) {
                request.headers.append(HttpHeaders.Authorization, "Bearer $accessToken")
                Log.d("HttpSendInterceptor", "Authorization header set with fresh token")
            } else {
                Log.d("HttpSendInterceptor", "No token available, request without auth")
            }

            // Execute request
            val originalCall = execute(request)

            // Handle 401 Unauthorized untuk token refresh
            if (originalCall.response.status == HttpStatusCode.Unauthorized) {
                Log.w("HttpSendInterceptor", "Received 401, attempting token refresh")

                val refreshToken = tokens?.refreshToken
                if (!refreshToken.isNullOrBlank()) {
                    try {
                        // Refresh token
                        val newTokens = refreshTokenFromServer(refreshToken)
                        dataStore.saveTokens(newTokens.accessToken, newTokens.refreshToken)

                        Log.d("HttpSendInterceptor", "Token refreshed successfully, retrying request")

                        // Retry request dengan token baru
                        request.headers.remove(HttpHeaders.Authorization)
                        request.headers.append(HttpHeaders.Authorization, "Bearer ${newTokens.accessToken}")

                        return@intercept execute(request)

                    } catch (e: Exception) {
                        Log.e("HttpSendInterceptor", "Token refresh failed: ${e.message}")
                        dataStore.clearDataStore()
                        return@intercept originalCall // Return original 401 response
                    }
                } else {
                    Log.w("HttpSendInterceptor", "No refresh token available")
                }
            }

            originalCall
        }
        return client
    }
}

private suspend fun refreshTokenFromServer(refreshToken: String): RefreshTokenResponse {
    val refreshClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    try {
        val response = refreshClient.post("https://spring.sanalab.live/api/auth/refresh") {
            contentType(ContentType.Application.Json)
            setBody(RefreshTokenRequest(refreshToken))
        }

        return response.body<RefreshTokenResponse>()
    } finally {
        refreshClient.close()
    }
}