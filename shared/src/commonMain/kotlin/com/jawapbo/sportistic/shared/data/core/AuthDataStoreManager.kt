package com.jawapbo.sportistic.shared.data.core

import com.jawapbo.sportistic.shared.data.auth.AuthDataStore
import com.jawapbo.sportistic.shared.data.auth.AuthTokens
import com.jawapbo.sportistic.shared.data.auth.User

interface AuthDataStoreManager {
    suspend fun saveTokens(accessToken: String, refreshToken: String)
    suspend fun saveUser(user: User)
    suspend fun saveAuth(
        tokens: AuthTokens,
        user: User
    )
    val user: User?
    suspend fun getUser(): User?
    suspend fun getTokens(): AuthDataStore
    suspend fun clearDataStore()
}