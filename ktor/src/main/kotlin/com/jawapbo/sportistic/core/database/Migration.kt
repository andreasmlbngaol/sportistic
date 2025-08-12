package com.jawapbo.sportistic.core.database

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
import org.jetbrains.exposed.v1.jdbc.JdbcTransaction
import org.jetbrains.exposed.v1.jdbc.SchemaUtils

data class Migration(
    val version: Int,
    val run: JdbcTransaction.() -> Unit
)

val migrations = listOf(
    Migration(1) {
        SchemaUtils.create(
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
    },
    Migration(2) {
        exec("""
            ALTER TABLE staffs DROP CONSTRAINT fk_staffs_user_id__id;
            ALTER TABLE staffs ADD CONSTRAINT fk_staffs_user_id__id
            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;
        """.trimIndent())

        exec("""
            ALTER TABLE staffs DROP CONSTRAINT fk_staffs_merchant_id__id;
            ALTER TABLE staffs ADD CONSTRAINT fk_staffs_merchant_id__id
            FOREIGN KEY (merchant_id) REFERENCES merchants(id) ON DELETE CASCADE;
        """.trimIndent())
    }
)
