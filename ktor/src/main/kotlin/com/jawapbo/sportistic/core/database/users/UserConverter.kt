package com.jawapbo.sportistic.core.database.users

import com.jawapbo.sportistic.shared.data.auth.User
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toUser() = User(
    id = this[Users.id],
    name = this[Users.name],
    email = this[Users.email],
    profilePictureUrl = this[Users.profilePictureUrl],
    method = this[Users.method],
    isEmailVerified = this[Users.isEmailVerified],
    createdAt = this[Users.createdAt]
)