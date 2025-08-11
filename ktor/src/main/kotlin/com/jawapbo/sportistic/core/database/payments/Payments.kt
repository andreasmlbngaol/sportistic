package com.jawapbo.sportistic.core.database.payments

import com.jawapbo.sportistic.core.data.PaymentMethod
import com.jawapbo.sportistic.core.data.PaymentStatus
import com.jawapbo.sportistic.core.database.bookings.Bookings
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object Payments: LongIdTable("payments") {
    val bookingId = reference("booking_id", Bookings.id)
    val amount = double("amount")
    val status = enumerationByName<PaymentStatus>("status", 32)
    val method = enumerationByName<PaymentMethod>("method", 32)
}