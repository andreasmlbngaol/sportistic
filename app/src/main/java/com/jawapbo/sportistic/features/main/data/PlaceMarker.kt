package com.jawapbo.sportistic.features.main.data

data class PlaceMarker(
    val id: Long,
    val name: String,
    val lat: Double,
    val lng: Double,
    val description: String? = null
)

val PLACES = listOf(
    PlaceMarker(
        id = 1,
        name = "Caritas",
        lat = 3.571975560442963,
        lng = 98.65209651509176,
        description = "Tempat latihan"
    ),
    PlaceMarker(
        id = 2,
        name = "Total Futsal",
        lat = 3.5693166248155523,
        lng = 98.6473142457537,
        description = "Tempat latihan 2"
    )
)