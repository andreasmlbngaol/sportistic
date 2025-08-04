package com.jawapbo.sportistic.features.auth.data

import com.jawapbo.sportistic.core.data.AuthTokens
import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val tokens: AuthTokens,
    val user: User
)