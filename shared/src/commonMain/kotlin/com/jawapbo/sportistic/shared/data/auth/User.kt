package com.jawapbo.sportistic.shared.data.auth

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Serializable
data class User(
    val id: String = "",
    val name: String = "",
    val email: String? = null,
    val profilePictureUrl: String? = null,
    val method: AuthMethod = AuthMethod.EMAIL_AND_PASSWORD,
    val isEmailVerified: Boolean = false,
    val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
)
