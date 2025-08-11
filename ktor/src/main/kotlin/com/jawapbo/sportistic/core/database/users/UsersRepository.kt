package com.jawapbo.sportistic.core.database.users

import com.jawapbo.sportistic.shared.data.auth.User
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object UsersRepository {
    fun findAll() = transaction {
        Users.selectAll()
            .map { it.toUser() }
    }

    fun findById(id: String) = transaction {
        Users.selectAll()
            .where { Users.id eq id }
            .map { it.toUser() }
            .firstOrNull()
    }

    fun save(user: User) = transaction {
        Users.insert {
            it[id] = user.id
            it[name] = user.name
            it[email] = user.email
            it[profilePictureUrl] = user.profilePictureUrl
            it[method] = user.method
            it[isEmailVerified] = user.isEmailVerified
            it[createdAt] = user.createdAt
        }
    }
}