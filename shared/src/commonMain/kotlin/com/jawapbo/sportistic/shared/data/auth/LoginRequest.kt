package com.jawapbo.sportistic.shared.data.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val idToken: String,
    val method: AuthMethod
)