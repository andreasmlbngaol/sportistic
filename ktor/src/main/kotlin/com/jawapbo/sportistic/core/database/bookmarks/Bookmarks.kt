package com.jawapbo.sportistic.core.database.bookmarks

import com.jawapbo.sportistic.core.database.merchants.Merchants
import com.jawapbo.sportistic.core.database.places.Places
import com.jawapbo.sportistic.core.database.users.Users
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object Bookmarks: LongIdTable("bookmarks") {
    val userId = reference("user_id", Users.id)
    val merchantId = reference("merchant_id", Merchants.id)

    init {
        index(true, userId, merchantId)
    }
}