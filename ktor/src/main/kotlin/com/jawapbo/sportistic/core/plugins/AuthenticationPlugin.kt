package com.jawapbo.sportistic.core.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.jawapbo.sportistic.core.data.AuthNames
import com.jawapbo.sportistic.shared.data.staffs.StaffRole
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt

fun Application.authenticationPlugin() {
    val secretKey = environment.config.config("ktor.jwt").property("secretKey").getString()
    install(Authentication) {
        jwt(AuthNames.CUSTOMER_JWT_AUTH) {
            realm = "Access to customer application"
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secretKey))
                    .build()
            )
            validate { credential ->
                val payload = credential.payload
                val id = payload.getClaim("sub")

                if (id != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }

        jwt(AuthNames.MERCHANT_JWT_AUTH) {
            realm = "Access to merchant application"
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secretKey))
                    .build()
            )
            validate { credential ->
                val payload = credential.payload
                val id = payload.getClaim("sub")
                val role = try {
                    StaffRole.valueOf(payload.getClaim("role").asString())
                } catch (_: Exception) {
                    null
                }

                if (id != null && role != null) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
        }
    }
}