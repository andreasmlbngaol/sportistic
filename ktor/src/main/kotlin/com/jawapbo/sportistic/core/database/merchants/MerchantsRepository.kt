package com.jawapbo.sportistic.core.database.merchants

import com.jawapbo.sportistic.shared.data.merchants.Merchant
import org.jetbrains.exposed.v1.jdbc.insertIgnoreAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object MerchantsRepository {
    fun save(merchant: Merchant) = transaction {
        Merchants.insertIgnoreAndGetId {
            it[name] = merchant.name
            it[address] = merchant.address
            it[latitude] = merchant.latitude
            it[longitude] = merchant.longitude
            it[imageUrl] = merchant.imageUrl
            it[description] = merchant.description
        }
    }?.value

    fun findAll() = transaction {
        Merchants
            .selectAll()
            .map { it.toMerchant() }
    }

    fun findById(id: Long) = transaction {
        Merchants
            .selectAll()
            .where { Merchants.id eq id }
            .map { it.toMerchant() }
            .singleOrNull()
    }

}