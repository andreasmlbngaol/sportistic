package com.jawapbo.sportistic.shared.data.auth

import kotlinx.serialization.Serializable

@Serializable
data class CustomerLoginResponse(
    val tokens: RefreshTokenResponse,
    val user: User
)