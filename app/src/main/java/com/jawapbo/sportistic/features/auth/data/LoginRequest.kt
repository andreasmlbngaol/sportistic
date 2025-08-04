package com.jawapbo.sportistic.features.auth.data

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val idToken: String,
    val method: AuthMethod = AuthMethod.EMAIL_AND_PASSWORD
)

@Suppress("unused")
enum class AuthMethod {
    EMAIL_AND_PASSWORD,
    GOOGLE,
    FACEBOOK,
    GITHUB,
    PHONE
}