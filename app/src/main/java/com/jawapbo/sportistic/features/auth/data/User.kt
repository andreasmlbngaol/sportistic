package com.jawapbo.sportistic.features.auth.data

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class User(
    val id: String = "",
    val name: String = "",
    val email: String? = null,
    val profilePictureUrl: String? = null,
    val method: AuthMethod = AuthMethod.EMAIL_AND_PASSWORD,
    val isEmailVerified: Boolean = false,
    @Contextual val createdAt: LocalDateTime = LocalDateTime.now()
)
