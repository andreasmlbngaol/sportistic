package com.jawapbo.sportistic.core.database.places

import com.jawapbo.sportistic.shared.data.places.Place
import org.jetbrains.exposed.v1.jdbc.insertIgnoreAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

object PlacesRepository {
    fun findAll() = transaction {
        Places.selectAll()
            .map { it.toPlace() }
    }

    fun findById(id: Long) = transaction {
        Places.selectAll()
            .where { Places.id eq id }
            .map { it.toPlace() }
            .firstOrNull()
    }

    fun save(place: Place) = transaction {
        Places.insertIgnoreAndGetId {
            it[name] = place.name
            it[address] = place.address
            it[latitude] = place.latitude
            it[longitude] = place.longitude
            it[imageUrl] = place.imageUrl
            it[description] = place.description
        }
    }
}