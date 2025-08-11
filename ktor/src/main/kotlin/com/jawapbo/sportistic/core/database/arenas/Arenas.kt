package com.jawapbo.sportistic.core.database.arenas

import com.jawapbo.sportistic.core.data.SportType
import com.jawapbo.sportistic.core.database.merchants.Merchants
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object Arenas: LongIdTable("arenas") {
    val merchantId = reference("merchant_id", Merchants.id)
    val type = enumerationByName<SportType>("sport_type", 32)
    val name = varchar("name", 100)
}