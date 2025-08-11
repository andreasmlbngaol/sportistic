package com.jawapbo.sportistic.shared.data.places

import kotlinx.serialization.Serializable

@Serializable
data class Place(
    val id: Long = 0L,
    val name: String = "",
    val address: String? = null,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val imageUrl: String? = null,
    val description: String? = null,
)
