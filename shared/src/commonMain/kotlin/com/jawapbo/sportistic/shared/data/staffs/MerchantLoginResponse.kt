package com.jawapbo.sportistic.shared.data.staffs

import com.jawapbo.sportistic.shared.data.auth.RefreshTokenResponse
import com.jawapbo.sportistic.shared.data.auth.User
import com.jawapbo.sportistic.shared.data.merchants.Merchant
import kotlinx.serialization.Serializable

@Serializable
data class MerchantLoginResponse(
    val tokens: RefreshTokenResponse,
    val user: User,
    val role: StaffRole,
    val merchant: Merchant
)