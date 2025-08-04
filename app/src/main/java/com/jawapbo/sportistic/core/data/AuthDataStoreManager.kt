package com.jawapbo.sportistic.core.data

import androidx.datastore.core.DataStore
import com.jawapbo.sportistic.features.auth.data.User
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class AuthDataStoreManager(
    private val dataStore: DataStore<AuthDataStore>
) {
    private suspend fun saveTokens(
        accessToken: String,
        refreshToken: String
    ) {
        dataStore.updateData { store ->
            store.copy(
                tokens = AuthTokens(
                    accessToken = accessToken,
                    refreshToken = refreshToken
                )
            )
        }
    }

    private suspend fun saveUser(
        userData: User
    ) {
        dataStore.updateData { store ->
            store.copy(
                user = userData
            )
        }
    }

    suspend fun saveAuth(
        tokens: AuthTokens,
        user: User
    ) {
        saveTokens(tokens.accessToken, tokens.refreshToken)
        saveUser(user)
    }

    val user: User?
        get() = runBlocking { getUser() }

    suspend fun getUser(): User? {
        val store = dataStore.data.first()
        if (store.user == null) return null

        return User(
            id = store.user.id,
            name = store.user.name,
            email = store.user.email,
            profilePictureUrl = store.user.profilePictureUrl,
            method = store.user.method,
            isEmailVerified = store.user.isEmailVerified,
            createdAt = store.user.createdAt
        )
    }

    suspend fun clearDataStore() {
        dataStore.updateData { store ->
            store.copy(
                tokens = null,
                user = null
            )
        }
    }

    suspend fun getTokens(): AuthDataStore {
        return dataStore.data.first()
    }
}