package com.jawapbo.sportistic.shared.data.staffs

import com.jawapbo.sportistic.shared.data.auth.User
import com.jawapbo.sportistic.shared.data.merchants.Merchant
import kotlinx.serialization.Serializable

@Serializable
data class StaffJoint(
    val id: Long = 0L,
    val user: User? = null,
    val merchant: Merchant = Merchant(),
    val role: StaffRole = StaffRole.Owner,
    val code: String = ""
)
