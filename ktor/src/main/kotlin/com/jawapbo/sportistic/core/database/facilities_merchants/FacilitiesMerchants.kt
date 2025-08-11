package com.jawapbo.sportistic.core.database.facilities_merchants

import com.jawapbo.sportistic.core.database.facilities.Facilities
import com.jawapbo.sportistic.core.database.merchants.Merchants
import org.jetbrains.exposed.v1.core.dao.id.LongIdTable

object FacilitiesMerchants: LongIdTable("facilities_merchants") {
    val facilityId = reference("facility_id", Facilities.id)
    val merchantId = reference("merchant_id", Merchants.id)
}