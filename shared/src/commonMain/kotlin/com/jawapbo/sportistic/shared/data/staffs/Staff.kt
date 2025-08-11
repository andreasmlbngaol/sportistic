package com.jawapbo.sportistic.shared.data.staffs

import kotlinx.serialization.Serializable

@Serializable
data class Staff(
    val id: Long = 0L,
    val userId: String? = null,
    val merchantId: Long = 0L,
    val role: StaffRole = StaffRole.Owner,
    val code: String = ""
)