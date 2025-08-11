package com.jawapbo.sportistic.core.database.places

import com.jawapbo.sportistic.shared.data.places.Place
import org.jetbrains.exposed.v1.core.ResultRow

fun ResultRow.toPlace(): Place {
    return Place(
        id = this[Places.id].value,
        name = this[Places.name],
        address = this[Places.address],
        latitude = this[Places.latitude],
        longitude = this[Places.longitude],
        imageUrl = this[Places.imageUrl],
        description = this[Places.description]
    )
}