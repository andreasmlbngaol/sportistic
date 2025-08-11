package com.jawapbo.sportistic.shared.data.merchants

import kotlinx.serialization.Serializable

@Serializable
data class CreateMerchantRequest(
    val name: String,
    val address: String? = null,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String? = null,
    val description: String? = null
)