package com.jawapbo.sportistic.core.data

import androidx.datastore.core.DataStore
import com.jawapbo.sportistic.shared.data.auth.AuthDataStore
import com.jawapbo.sportistic.shared.data.auth.AuthTokens
import com.jawapbo.sportistic.shared.data.auth.User
import com.jawapbo.sportistic.shared.data.core.AuthDataStoreManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class AuthDataStoreManagerImpl(
    private val dataStore: DataStore<AuthDataStore>
): AuthDataStoreManager {
    override suspend fun saveTokens(
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

    override suspend fun saveUser(
        user: User
    ) {
        dataStore.updateData { store ->
            store.copy(
                user = user
            )
        }
    }

    override suspend fun saveAuth(
        tokens: AuthTokens,
        user: User
    ) {
        saveTokens(tokens.accessToken, tokens.refreshToken)
        saveUser(user)
    }

    override val user: User?
        get() = runBlocking { getUser() }

    override suspend fun getUser(): User? {
        val store = dataStore.data.first()

        store.user?.let {
            return User(
                id = it.id,
                name = it.name,
                email = it.email,
                profilePictureUrl = it.profilePictureUrl,
                method = it.method,
                isEmailVerified = it.isEmailVerified,
                createdAt = it.createdAt
            )
        }

        return null
    }

    override suspend fun clearDataStore() {
        dataStore.updateData { store ->
            store.copy(
                tokens = null,
                user = null
            )
        }
    }


    override suspend fun getTokens(): AuthDataStore {
        return dataStore.data.first()
    }
}