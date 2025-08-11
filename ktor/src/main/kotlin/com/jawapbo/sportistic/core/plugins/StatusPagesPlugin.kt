package com.jawapbo.sportistic.core.plugins

import com.jawapbo.sportistic.core.utils.respondJson
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.statuspages.StatusPages

fun Application.statusPagesPlugin() {
    install(StatusPages) {
        exception<BadRequestException> { call, cause ->
            cause.printStackTrace()
            call.respondJson(HttpStatusCode.BadRequest, "Invalid Request Body")
        }
        exception<Throwable> { call, cause ->
            cause.printStackTrace()
            call.respondJson(HttpStatusCode.InternalServerError, cause::class.qualifiedName ?: "Unknown error")
        }
    }
}