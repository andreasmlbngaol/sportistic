package com.jawapbo.sportistic.core.model

import android.util.Log
import com.jawapbo.sportistic.shared.data.auth.LoginRequest
import com.jawapbo.sportistic.shared.data.auth.LoginResponse
import com.jawapbo.sportistic.shared.data.merchants.Merchant
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.isSuccess

class SportisticRepository(
    private val httpClient: HttpClient
) {
    @Suppress("PrivatePropertyName")
    private val BASE_URL = "https://spring.sanalab.live/api"

    suspend fun login(body: LoginRequest): LoginResponse? {
        try {
            val response = httpClient.post("$BASE_URL/auth/login") {
                setBody(body)
            }

            if(response.status.isSuccess()) {
                return response.body<LoginResponse>()
            }
            return null
        } catch (e: Exception) {
            Log.e("SportisticRepository", "Error login: ${e.message}")
            return null
        }
    }

    suspend fun addMerchantToBookmark(id: Long): List<Long>? {
        try {
            val response = httpClient.post("$BASE_URL/bookmarks/$id")
            return response.body<List<Long>>()
        } catch (e: Exception) {
            Log.e("SportisticRepository", "Error addMerchantToBookmark: ${e.message}")
            return null
        }
    }

    suspend fun removeMerchantFromBookmark(id: Long): List<Long>? {
        try {
            val response = httpClient.delete("$BASE_URL/bookmarks/$id")
            return response.body<List<Long>>()
        } catch (e: Exception) {
            Log.e("SportisticRepository", "Error removeMerchantFromBookmark: ${e.message}")
            return null
        }
    }

    suspend fun getMerchants(): List<Merchant>? {
        try {
            val response = httpClient.get("$BASE_URL/merchants")
            return response.body<List<Merchant>>()
        } catch (e: Exception) {
            Log.e("SportisticRepository", "Error getMerchants: ${e.message}")
            return null
        }
    }

    suspend fun getBookmarkedMerchants(): List<Long>? {
        try {
            val response = httpClient.get("$BASE_URL/bookmarks")
            return response.body<List<Long>>()
        } catch (e: Exception) {
            Log.e("SportisticRepository", "Error getBookmarkedMerchants: ${e.message}")
            return null
        }
    }
}