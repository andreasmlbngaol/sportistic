package com.jawapbo.sportistic.main.places

import com.jawapbo.sportistic.core.database.places.PlacesRepository
import com.jawapbo.sportistic.core.utils.respondJson
import com.jawapbo.sportistic.shared.data.places.CreatePlaceRequest
import com.jawapbo.sportistic.shared.data.places.Place
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.placeRoute() {
    route("/places") {
        get {
            call.respond(
                PlacesRepository.findAll()
            )
        }

        post {
            val request = call.receive<CreatePlaceRequest>()
            val place = Place(
                name = request.name,
                address = request.address,
                latitude = request.latitude,
                longitude = request.longitude,
                imageUrl = request.imageUrl,
                description = request.description
            )

            val id = PlacesRepository.save(place)
                ?: return@post call.respondJson(HttpStatusCode.InternalServerError, "Failed to save place")

            call.respond(id.value)
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: return@get call.respondJson(
                HttpStatusCode.BadRequest, "Place ID is required"
            )

            val place = PlacesRepository.findById(id)
                ?: return@get call.respondJson(HttpStatusCode.NotFound, "Place not found")

            call.respond(place)
        }

    }
}