package com.jawapbo.sportistic.shared.data.core

import com.jawapbo.sportistic.shared.data.auth.RefreshTokenRequest
import com.jawapbo.sportistic.shared.data.auth.RefreshTokenResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy

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
//                        serializersModule = SerializersModule {
//                            contextual(LocalDateTime::class, LocalDateTimeSerializer)
//                        }
                    }
                )
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
        client.plugin(HttpSend).intercept { request ->
            println("HttpSendInterceptor, Intercepting request to ${request.url}")

            // Get fresh token dari DataStore untuk setiap request
            val tokens =  dataStore.getTokens().tokens
            val accessToken = tokens?.accessToken

            println("HttpSendInterceptor, Current stored token: ${accessToken?.take(20)}...")

            // Remove existing Authorization header jika ada
            request.headers.remove(HttpHeaders.Authorization)

            // Set token yang fresh jika ada
            if (!accessToken.isNullOrBlank()) {
                request.headers.append(HttpHeaders.Authorization, "Bearer $accessToken")
                println("HttpSendInterceptor, Authorization header set with fresh token")
            } else {
                println("HttpSendInterceptor, No token available, request without auth")
            }

            // Execute request
            val originalCall = execute(request)

            // Handle 401 Unauthorized untuk token refresh
            if (originalCall.response.status == HttpStatusCode.Unauthorized) {
                println("HttpSendInterceptor, Received 401, attempting token refresh")

                val refreshToken = tokens?.refreshToken
                if (!refreshToken.isNullOrBlank()) {
                    try {
                        // Refresh token
                        val newTokens = refreshTokenFromServer(refreshToken)
                        dataStore.saveTokens(newTokens.accessToken, newTokens.refreshToken)

                        println("HttpSendInterceptor, Token refreshed successfully, retrying request")

                        // Retry request dengan token baru
                        request.headers.remove(HttpHeaders.Authorization)
                        request.headers.append(HttpHeaders.Authorization, "Bearer ${newTokens.accessToken}")

                        return@intercept execute(request)

                    } catch (e: Exception) {
                        println("HttpSendInterceptor, Token refresh failed: ${e.message}")
                        dataStore.clearDataStore()
                        return@intercept originalCall // Return original 401 response
                    }
                } else {
                    println("HttpSendInterceptor, No refresh token available")
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