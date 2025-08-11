package com.jawapbo.sportistic.core.database.facilities

import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object Facilities: LongIdTable("facilities") {
    val name = varchar("facilities", 100)
    val description = text("description").nullable()
}