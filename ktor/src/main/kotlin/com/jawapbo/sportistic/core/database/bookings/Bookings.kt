package com.jawapbo.sportistic.core.database.bookings

import com.jawapbo.sportistic.core.data.BookingStatus
import com.jawapbo.sportistic.core.database.users.Users
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.CurrentDateTime
import org.jetbrains.exposed.v1.datetime.datetime

object Bookings: LongIdTable("bookings") {
    val userId = reference("user_id", Users.id)
    val status = enumerationByName<BookingStatus>("status", 32)
    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val paidAt = datetime("paid_at").nullable()
}