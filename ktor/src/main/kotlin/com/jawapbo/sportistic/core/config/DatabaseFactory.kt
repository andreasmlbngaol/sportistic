package com.jawapbo.sportistic.core.config

import com.jawapbo.sportistic.core.database.arenas.Arenas
import com.jawapbo.sportistic.core.database.bookings.Bookings
import com.jawapbo.sportistic.core.database.bookings_items.BookingsItems
import com.jawapbo.sportistic.core.database.bookmarks.Bookmarks
import com.jawapbo.sportistic.core.database.courts.Courts
import com.jawapbo.sportistic.core.database.facilities.Facilities
import com.jawapbo.sportistic.core.database.facilities_merchants.FacilitiesMerchants
import com.jawapbo.sportistic.core.database.merchants.Merchants
import com.jawapbo.sportistic.core.database.payments.Payments
import com.jawapbo.sportistic.core.database.places.Places
import com.jawapbo.sportistic.core.database.staffs.Staffs
import com.jawapbo.sportistic.core.database.users.Users
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.ApplicationConfig
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object DatabaseFactory {
    fun init(config: ApplicationConfig) {
        val dbConfig = config.config("ktor.database")
        println("URL: " + dbConfig.property("url").getString())
        println("User: " + dbConfig.property("user").getString())


        val hikariConfig = HikariConfig().apply {
            jdbcUrl = dbConfig.property("url").getString()
            driverClassName = dbConfig.property("driver").getString()
            username = dbConfig.property("user").getString()
            password = dbConfig.property("password").getString()
            maximumPoolSize = dbConfig.property("maxPoolSize").getString().toInt()
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        }
        val datasource = HikariDataSource(hikariConfig)
        Database.connect(datasource)
        createTableIfNotExist()

    }

    fun createTableIfNotExist() {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(
                Arenas,
                Bookings,
                BookingsItems,
                Bookmarks,
                Courts,
                Facilities,
                FacilitiesMerchants,
                Merchants,
                Payments,
                Places,
                Staffs,
                Users
            )
        }

    }
}