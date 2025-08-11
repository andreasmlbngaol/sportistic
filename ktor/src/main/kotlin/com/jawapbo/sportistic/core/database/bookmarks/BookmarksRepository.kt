package com.jawapbo.sportistic.core.database.bookmarks

import org.jetbrains.exposed.v1.core.SqlExpressionBuilder.eq
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.jdbc.deleteWhere
import org.jetbrains.exposed.v1.jdbc.insertIgnoreAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object BookmarksRepository {
    fun findAllByUserId(userId: String) = transaction {
        Bookmarks.selectAll()
            .where { Bookmarks.userId eq userId }
            .map { it[Bookmarks.merchantId].value }
    }

    fun findAllByMerchantId(merchantId: Long) = transaction {
        Bookmarks.selectAll()
            .where { Bookmarks.merchantId eq merchantId }
            .map { it[Bookmarks.userId] }
    }

    fun saveBookmark(userId: String, merchantId: Long) = transaction {
        Bookmarks.insertIgnoreAndGetId {
            it[Bookmarks.userId] = userId
            it[Bookmarks.merchantId] = merchantId
        }
    }

    fun deleteBookmark(userId: String, merchantId: Long) = transaction {
        Bookmarks.deleteWhere {
            (Bookmarks.userId eq userId) and (Bookmarks.merchantId eq merchantId)
        }
    }
}