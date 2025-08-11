package com.jawapbo.sportistic.core.database.bookings_items

import com.jawapbo.sportistic.core.database.bookings.Bookings
import com.jawapbo.sportistic.core.database.courts.Courts
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable
import org.jetbrains.exposed.v1.datetime.date
import org.jetbrains.exposed.v1.datetime.time

object BookingsItems: LongIdTable("bookings_items") {
    val bookingId = reference("booking_id", Bookings.id)
    val courtId = reference("court_id", Courts.id)
    val date = date("date")
    val startTime = time("start_time")
    val endTime = time("end_time")
    val price = double("price")
}