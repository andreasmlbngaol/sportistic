package com.jawapbo.sportistic.customers.merchants

import com.jawapbo.sportistic.core.database.merchants.MerchantsRepository
import com.jawapbo.sportistic.core.database.staffs.StaffsRepository
import com.jawapbo.sportistic.core.utils.respondJson
import com.jawapbo.sportistic.shared.data.merchants.CreateMerchantRequest
import com.jawapbo.sportistic.shared.data.merchants.MerchantCodeResponse
import com.jawapbo.sportistic.shared.data.merchants.Merchant
import com.jawapbo.sportistic.shared.data.staffs.Staff
import com.jawapbo.sportistic.shared.data.staffs.StaffRole
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.merchantRoute() {
    route("/merchants") {
        post {
            val request = call.receive<CreateMerchantRequest>()

            val merchant = Merchant(
                name = request.name,
                address = request.address,
                latitude = request.latitude,
                longitude = request.longitude,
                imageUrl = request.imageUrl,
                description = request.description
            )

            val merchantId = MerchantsRepository.save(merchant)
                ?: return@post call.respondJson(HttpStatusCode.InternalServerError, "Failed to save merchant")

            val code = generateUniqueStaffCode()

            val staff = Staff(
                merchantId = merchantId,
                role = StaffRole.Owner,
                code = code
            )

            StaffsRepository.save(staff)
                ?: return@post call.respondJson(HttpStatusCode.InternalServerError, "Failed to save staff")

            call.respond(MerchantCodeResponse(code))
        }

        get {
            call.respond(MerchantsRepository.findAll())
        }

        get("/{id}") {
            val id = call.parameters["id"]?.toLongOrNull() ?: return@get call.respondJson(
                HttpStatusCode.BadRequest, "Merchant ID is required"
            )

            val merchant = MerchantsRepository.findById(id)
                ?: return@get call.respondJson(HttpStatusCode.NotFound, "Merchant not found")

            call.respond(merchant)
        }
    }
}

private fun generateStaffCode(): String {
    val chars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..8)
        .map { chars.random() }
        .joinToString("")
}

private fun generateUniqueStaffCode(): String {
    while (true) {
        val code = generateStaffCode()
        if (!StaffsRepository.existByCode(code)) return code
    }
}
