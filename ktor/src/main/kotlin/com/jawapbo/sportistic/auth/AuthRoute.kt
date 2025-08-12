package com.jawapbo.sportistic.auth

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.jawapbo.sportistic.core.config.JwtConfig
import com.jawapbo.sportistic.core.database.merchants.MerchantsRepository
import com.jawapbo.sportistic.core.database.staffs.StaffsRepository
import com.jawapbo.sportistic.core.database.users.UsersRepository
import com.jawapbo.sportistic.core.utils.respondJson
import com.jawapbo.sportistic.shared.data.auth.*
import com.jawapbo.sportistic.shared.data.staffs.MerchantLoginResponse
import com.jawapbo.sportistic.shared.data.staffs.RegisterStaffRequest
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

fun Route.authRoute() {
    route("/auth") {
        post("/customer/login") {
            val user = validateOrCreateUser()
                ?: return@post call.respondJson(HttpStatusCode.BadRequest, "Invalid Request")

            val accessToken = JwtConfig.generateCustomerAccessToken(user)
            val refreshToken = JwtConfig.generateRefreshToken(user.id)

            call.respond(
                CustomerLoginResponse(
                    tokens = RefreshTokenResponse(accessToken, refreshToken),
                    user = user
                )
            )
        }

        post("/merchant/login") {
            val user = validateOrCreateUser()
                ?: return@post call.respondJson(HttpStatusCode.BadRequest, "Invalid Request")

            val staff = StaffsRepository.findByUserId(user.id)
                ?: return@post call.respondJson(HttpStatusCode.NotFound, "User is not a staff")

            val merchant = MerchantsRepository.findById(staff.merchantId)
                ?: return@post call.respondJson(HttpStatusCode.NotFound, "Merchant not found")

            val accessToken = JwtConfig.generateStaffAccessToken(user, staff.role, staff.merchantId)
            val refreshToken = JwtConfig.generateRefreshToken(user.id)

            call.respond(
                MerchantLoginResponse(
                    tokens = RefreshTokenResponse(accessToken, refreshToken),
                    user = user,
                    role = staff.role,
                    merchant = merchant
                )
            )
        }

        post("/merchant/register") {
            val payload = call.receive<RegisterStaffRequest>()
            val code = payload.enrollCode

            val staff = StaffsRepository.findByCode(code)
                ?: return@post call.respondJson(HttpStatusCode.NotFound, "Invalid enroll code")
            if(staff.user?.id != null) {
                return@post call.respondJson(HttpStatusCode.Conflict, "Staff has been registered")
            }

            val decoded = withContext(Dispatchers.IO) {
                try {
                    FirebaseAuth.getInstance().verifyIdToken(payload.loginRequest.idToken)
                } catch (e: FirebaseAuthException) {
                    throw IllegalArgumentException("Invalid ID token: ${e.message}", e)
                }
            }

            val uid = decoded.uid
            val user = UsersRepository.findById(uid) ?: run {
                println("User with ID $uid not found, creating new user.")

                User(
                    id = uid,
                    email = decoded.email.orEmpty(),
                    name = decoded.name,
                    profilePictureUrl = decoded.picture,
                    isEmailVerified = decoded.isEmailVerified,
                    method = payload.loginRequest.method
                ).also { UsersRepository.save(it) }
            }

            StaffsRepository.enrollUserToStaff(
                staffId = staff.id,
                userId = user.id
            )
                .takeIf { it > 0 }
                ?: return@post call.respondJson(HttpStatusCode.InternalServerError, "Failed to enroll user to staff")

            val accessToken = JwtConfig.generateStaffAccessToken(user, staff.role, staff.merchant.id)
            val refreshToken = JwtConfig.generateRefreshToken(user.id)


            call.respond(
                MerchantLoginResponse(
                    tokens = RefreshTokenResponse(accessToken, refreshToken),
                    user = user,
                    role = staff.role,
                    merchant = staff.merchant
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

            val accessToken = JwtConfig.generateCustomerAccessToken(user)
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

private suspend fun RoutingContext.validateOrCreateUser(): User? {
    val payload = call.receive<LoginRequest>()

    val idToken = payload.idToken
    val method = payload.method

    val decoded = try {
        FirebaseAuth.getInstance().verifyIdToken(idToken)
    } catch (e: FirebaseAuthException) {
        println("Error verifying ID token: ${e.message}")
        return null
    }

    val uid = decoded.uid

    return UsersRepository.findById(uid) ?: run {
        println("User with ID $uid not found, creating new user.")

        User(
            id = uid,
            email = decoded.email ?: "",
            name = decoded.name,
            profilePictureUrl = decoded.picture,
            isEmailVerified = decoded.isEmailVerified,
            method = method
        ).also { UsersRepository.save(it) }
    }
}