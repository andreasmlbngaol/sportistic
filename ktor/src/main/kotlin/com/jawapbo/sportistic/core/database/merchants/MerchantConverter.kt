package com.jawapbo.sportistic.core.database.merchants

import com.jawapbo.sportistic.shared.data.merchants.Merchant
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toMerchant() = Merchant(
    id = this[Merchants.id].value,
    name = this[Merchants.name],
    address = this[Merchants.address],
    latitude = this[Merchants.latitude],
    longitude = this[Merchants.longitude],
    imageUrl = this[Merchants.imageUrl],
    description = this[Merchants.description]
)