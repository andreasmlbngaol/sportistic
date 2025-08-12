package com.jawapbo.sportistic.shared.data.staffs

import com.jawapbo.sportistic.shared.data.auth.LoginRequest
import kotlinx.serialization.Serializable

@Serializable
data class RegisterStaffRequest(
    val loginRequest: LoginRequest,
    val enrollCode: String
)