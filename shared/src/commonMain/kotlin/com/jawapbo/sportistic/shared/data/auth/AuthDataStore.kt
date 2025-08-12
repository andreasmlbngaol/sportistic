package com.jawapbo.sportistic.shared.data.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthDataStore(
    val tokens: AuthTokens? = null,
    val user: User? = null
)