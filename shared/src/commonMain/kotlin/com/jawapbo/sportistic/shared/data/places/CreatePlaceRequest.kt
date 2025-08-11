package com.jawapbo.sportistic.shared.data.places

import kotlinx.serialization.Serializable

@Serializable
data class CreatePlaceRequest(
    val name: String,
    val address: String? = null,
    val latitude: Double,
    val longitude: Double,
    val imageUrl: String? = null,
    val description: String? = null
)