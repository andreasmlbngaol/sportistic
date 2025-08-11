package com.jawapbo.sportistic.core.database.places

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object Places: LongIdTable("places") {
    val name = varchar("name", 100)
    val address = varchar("address", 255).nullable()
    val latitude = double("latitude")
    val longitude = double("longitude")
    val imageUrl = varchar("image_url", 255).nullable()
    val description = text("description").nullable()
}
