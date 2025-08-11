package com.jawapbo.sportistic.main.bookmarks

import com.jawapbo.sportistic.core.config.userId
import com.jawapbo.sportistic.core.database.bookmarks.BookmarksRepository
import com.jawapbo.sportistic.core.database.users.UsersRepository
import com.jawapbo.sportistic.core.utils.respondJson
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.bookmarkRoute() {
    route("/bookmarks") {
        get {
            val userId = call.principal<JWTPrincipal>()!!.userId
            if (UsersRepository.findById(userId) == null) {
                return@get call.respondJson(HttpStatusCode.BadRequest, "User not found")
            }

            call.respond(
                BookmarksRepository.findAllByUserId(userId)
            )
        }

        post("/{placeId}") {
            val userId = validUserId() ?: return@post
            val placeId = call.parameters["placeId"]?.toLongOrNull()
                ?: return@post call.respondJson(HttpStatusCode.BadRequest, "Invalid place ID")

            BookmarksRepository.saveBookmark(userId, placeId)
                ?: return@post call.respondJson(HttpStatusCode.InternalServerError, "Failed to bookmark place")

            call.respond(BookmarksRepository.findAllByUserId(userId))
        }

        delete("/{placeId}") {
            val userId = validUserId() ?: return@delete
            val placeId = call.parameters["placeId"]?.toLongOrNull()
                ?: return@delete call.respondJson(HttpStatusCode.BadRequest, "Invalid place ID")

            val deletedCount = BookmarksRepository.deleteBookmark(userId, placeId)
            if (deletedCount < 1) {
                return@delete call.respondJson(HttpStatusCode.InternalServerError, "Failed to delete bookmark")
            }

            call.respond(BookmarksRepository.findAllByUserId(userId))
        }
    }
}

suspend fun RoutingContext.validUserId(): String? {
    val userId = call.principal<JWTPrincipal>()?.userId
    if (userId == null || UsersRepository.findById(userId) == null) {
        call.respondJson(HttpStatusCode.BadRequest, "User not found")
        return null
    }
    return userId
}