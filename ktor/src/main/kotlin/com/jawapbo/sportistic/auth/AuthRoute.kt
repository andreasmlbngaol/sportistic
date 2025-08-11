package com.jawapbo.sportistic.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.jawapbo.sportistic.core.config.JwtConfig
import com.jawapbo.sportistic.core.database.users.UsersRepository
import com.jawapbo.sportistic.core.utils.respondJson
import com.jawapbo.sportistic.shared.data.auth.RefreshTokenResponse
import com.jawapbo.sportistic.shared.data.auth.LoginRequest
import com.jawapbo.sportistic.shared.data.auth.LoginResponse
import com.jawapbo.sportistic.shared.data.auth.RefreshTokenRequest
import com.jawapbo.sportistic.shared.data.auth.User
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.authRoute() {
    route("/auth") {
        post("/login") {
            val payload = call.receive<LoginRequest>()
            val idToken = payload.idToken
            val method = payload.method

            val decoded = try {
                FirebaseAuth.getInstance().verifyIdToken(idToken)
            } catch (e: FirebaseAuthException) {
                throw IllegalArgumentException("Invalid ID token: ${e.message}", e)
            }

            val uid = decoded.uid
            var user = UsersRepository.findById(uid)
            if (user == null) {
                println("User with ID $uid not found, creating new user.")

                user = User(
                    id = uid,
                    email = decoded.email ?: "",
                    name = decoded.name,
                    profilePictureUrl = decoded.picture,
                    isEmailVerified = decoded.isEmailVerified,
                    method = method
                )

                UsersRepository.save(user)
            }

            val accessToken = JwtConfig.generateAccessToken(user)
            val refreshToken = JwtConfig.generateRefreshToken(uid)

            call.respond(
                LoginResponse(
                    tokens = RefreshTokenResponse(accessToken, refreshToken),
                    user = user
                )
            )
        }

        post("/refresh") {
            val payload = call.receive<RefreshTokenRequest>()
            val tokenValid = JwtConfig.validateRefreshToken(payload.refreshToken)
            if (!tokenValid) {
                call.respondJson(HttpStatusCode.BadRequest, "Invalid refresh token")
            }

            val userId = JwtConfig.getUserIdFromToken(payload.refreshToken)
                ?: return@post call.respondJson(HttpStatusCode.BadRequest, "Invalid refresh token")

            val user = UsersRepository.findById(userId)
                ?: return@post call.respondJson(HttpStatusCode.NotFound, "User not found")

            val accessToken = JwtConfig.generateAccessToken(user)
            val newRefreshToken = JwtConfig.generateRefreshToken(userId)

            call.respond(
                RefreshTokenResponse(
                    accessToken = accessToken,
                    refreshToken = newRefreshToken
                )
            )
        }
    }
}
