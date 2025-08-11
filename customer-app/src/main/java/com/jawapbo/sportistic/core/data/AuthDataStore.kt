package com.jawapbo.sportistic.core.data

import com.jawapbo.sportistic.shared.data.auth.User
import kotlinx.serialization.Serializable

@Serializable
data class AuthDataStore(
    val tokens: AuthTokens? = null,
    val user: User? = null
)